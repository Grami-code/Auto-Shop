package com.serviceauto.services;

import com.serviceauto.services.OpenAIService;

public class TestAI {
    public static void main(String[] args) {
        String raspuns = OpenAIService.cereAI("Salut, verifica conexiunea!");
        System.out.println("Răspuns AI: " + raspuns);
    }
}