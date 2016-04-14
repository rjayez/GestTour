/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.util.List;

/**
 *
 * @author Romain
 */
public class GestTourUtils
{
    
    public static int calculerPGCD(List<Integer> listNombre)
    {
        if( listNombre.isEmpty())
            return 0;
        
        if(listNombre.size() == 1)
            return listNombre.get(0);
            
            
        int ppcm = calculerPGCD(listNombre.get(0), listNombre.get(1));
        
        
        
        for (int i = 2; i < listNombre.size(); i++)
        {
            ppcm = calculerPGCD(ppcm, listNombre.get(i));
        }
        return ppcm;
        
    }
    
    public static int calculerPGCD(int a, int b)
    {
        // plus grande valeur dans a
        if( a < b)
        {
            //swap
            int temp = b;
            b = a;
            a = temp;
        }
        
        if(b == 0)
            return 0;
        
        int produit = a*b;
        int reste = a - (a/b)*b;
        while(reste != 0)
        {
            a = b;
            b = reste;
            reste = a - (a/b)*b;
        }
        
        return produit/b;
    }
    
    public static String getNomFichierSansExtension(String cheminFichier)
    {
            File file = new File(cheminFichier);
            return file.getName().replaceFirst(".slr$", "");
    }
    
}
