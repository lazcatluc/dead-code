package com.aurea.und.locate;

import org.springframework.stereotype.Service;

@Service
public class PrivateMethod extends EntityLocator {

    @Override
    protected String entityLocator() {
        return "private method";
    }
    
}
