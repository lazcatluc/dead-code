package com.aurea.und.locate;

import org.springframework.stereotype.Service;

import com.scitools.understand.Entity;

@Service
public class FileLocator {
    public Entity getFile(Entity original) {
        return original.refs(null, null, true)[0].file();
    }
}
