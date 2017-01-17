package com.aurea.und.locate;

import org.springframework.stereotype.Service;

@Service
public class Parameter extends EntityLocator {

    @Override
    protected String entityLocator() {
        return "parameter";
    }

}
