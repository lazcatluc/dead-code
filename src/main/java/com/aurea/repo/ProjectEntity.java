package com.aurea.repo;

import com.scitools.understand.Reference;

public interface ProjectEntity {
    String[] ib(String descriptor);
    String name();
    String longname(boolean bool);
    Reference[] refs(String descriptor1, String descriptor2, boolean bool);
    
}
