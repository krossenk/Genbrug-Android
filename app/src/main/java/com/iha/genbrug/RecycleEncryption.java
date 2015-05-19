/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iha.genbrug;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morten
 */
public class RecycleEncryption 
{
    public static String getSecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try 
        {
            //create a new message digest of instance type MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RecycleEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedPassword;
    }
        
    public static String getSalt() throws NoSuchAlgorithmException,java.security.NoSuchProviderException
    {        
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        
        return salt.toString();
    }
}
