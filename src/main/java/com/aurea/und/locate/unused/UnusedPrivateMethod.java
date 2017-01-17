package com.aurea.und.locate.unused;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurea.und.locate.FileLocator;
import com.aurea.und.locate.PrivateMethod;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;

@Service
public class UnusedPrivateMethod extends PrivateMethod {

    private static final Logger LOGGER = Logger.getLogger(UnusedPrivateMethod.class);

    @Autowired
    private FileLocator fileLocator;

    @Override
    protected boolean matches(Entity entity) {
        LOGGER.info("Trying entity " + entity.name());
        Entity topClass = entity;

        Set<Reference> recursiveReferences = recursiveReferences(topClass, entity, new HashSet<>());
        recursiveReferences.forEach(ref -> LOGGER.info("entity referenced: " + ref.ent().longname(true) + "; "
                + ref.ent().id() + "; " + ref.ent().kind() + "; " + ref.file().name() + ":" + ref.line() + ":"
                + ref.column() + "; entity referring=" + ref.scope().longname(true)));
        return onlyInnerRecursiveReferences(recursiveReferences);
    }

    private boolean onlyInnerRecursiveReferences(Set<Reference> recursiveReferences) {
        return recursiveReferences.stream().allMatch(ref -> ref.ent().equals(ref.scope()));
    }

    private Set<Reference> recursiveReferences(Entity topEntity, Entity entity, Set<Entity> alreadyParsed) {
        LOGGER.debug("Looking in " + topEntity.name());
        Set<Reference> references = new HashSet<>();

        Reference[] refs = topEntity.refs(null, null, false);
        Reference start = refs[0];
        Reference end = refs[refs.length - 1];
        Arrays.stream(refs).forEach(reference -> {
            Entity referencedEntity = reference.ent();
            LOGGER.debug("Referencing " + referencedEntity.name());
            if (!isOwnEntity(referencedEntity)) {
                LOGGER.debug("Not own entity");
                return;
            }
            if (entity.equals(referencedEntity) && isBefore(start, reference) && isBefore(reference, end)) {
                LOGGER.debug("Found reference!");
                references.add(reference);
            }
            if (referencedEntity.kind().check("method") || referencedEntity.kind().check("class")) {
                if (!alreadyParsed.add(referencedEntity)) {
                    LOGGER.debug("Already parsed");
                    return;
                }
                references.addAll(recursiveReferences(referencedEntity, entity, alreadyParsed));
            } else {
                LOGGER.debug("Kind: " + referencedEntity.kind());
            }
        });

        return references;
    }
    
    protected boolean isBefore(Reference first, Reference second) {
        return first.line() < second.line() || (first.line() == second.line() && first.column() < second.column());
    }

}
