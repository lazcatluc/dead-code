package com.aurea.und.locate;

import org.springframework.stereotype.Service;

@Service
public class PrivateVariable extends EntityLocator {
    @Override
    protected String entityLocator() {
        return "private variable";
    }
}
