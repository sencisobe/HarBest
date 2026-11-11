package com.sencisobe.harbest.domain.service;

public interface TokenHasher {  
    String hash(String rawToken);
}