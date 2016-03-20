/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Romain
 */
public class GestTourUtilsTest
{
    
    public GestTourUtilsTest()
    {
    }

    @Test
    public void testCalculerPGCD_List()
    {
        System.out.println("calculerPGCD");
        List<Integer> listNombre = new ArrayList<>();
        listNombre.add(3);
        listNombre.add(5);
        listNombre.add(7);
        int expResult = 3 * 5 * 7;
        int result = GestTourUtils.calculerPGCD(listNombre);
        assertEquals(expResult, result);
        
    }
    
        @Test
    public void testCalculerPGCD_List2()
    {
        System.out.println("calculerPGCD");
        List<Integer> listNombre = new ArrayList<>();
        listNombre.add(4);
        listNombre.add(26);
        listNombre.add(12);
        int expResult = 156;
        int result = GestTourUtils.calculerPGCD(listNombre);
        assertEquals(expResult, result);
        
    }

    @Test
    public void testCalculerPGCD_int_int()
    {
        System.out.println("calculerPGCD");
        int a = 26;
        int b = 4;
        int expResult = 52;
        int result = GestTourUtils.calculerPGCD(a, b);
        assertEquals(expResult, result);
        
    }
    
}
