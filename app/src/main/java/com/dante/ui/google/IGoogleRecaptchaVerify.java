package com.dante.ui.google;

public interface IGoogleRecaptchaVerify {

    void testV9Porn();

    void verifyGoogleRecaptcha(String action, String r, String id, String recaptcha);

    String getBaseAddress();
}