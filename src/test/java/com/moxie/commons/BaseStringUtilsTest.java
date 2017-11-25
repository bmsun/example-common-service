package com.moxie.commons;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wangyanbo on 16/12/9.
 */
public class BaseStringUtilsTest {
    @Test
    public void testCompare() {
        String s1 = "1234";
        String s2 = "1234";
        Assert.assertEquals(1, BaseStringUtils.compare(s1, s2, -1));
        s1 = "12*4";
        Assert.assertEquals(0, BaseStringUtils.compare(s1, s2, -1));
        Assert.assertEquals(1, BaseStringUtils.compare(s1, s2, 3));
        s2 = "1#34";
        Assert.assertEquals(0, BaseStringUtils.compare(s1, s2, -1));
        Assert.assertEquals(1, BaseStringUtils.compare(s1, s2, 2));
        s1 = "****";
        Assert.assertEquals(0, BaseStringUtils.compare(s1, s2, -1));
        s1 = "*******";
        Assert.assertEquals(0, BaseStringUtils.compare(s1, s2, -1));
        s1 = "*******###";
        Assert.assertEquals(0, BaseStringUtils.compare(s1, s2, -1));
        s1 = "*******###&&";
        Assert.assertEquals(-1, BaseStringUtils.compare(s1, s2, -1));
        s1 = "1#345";
        Assert.assertEquals(-1, BaseStringUtils.compare(s1, s2, -1));
    }

    @Test
    public void testMergeSave() {
        BaseStringUtils.mergeSave(" ", null);
        String s1 = "1234";
        String s2 = "1234";
        Assert.assertEquals("1234", BaseStringUtils.mergeSave(s1, s2));
        s1 = "12*4";
        Assert.assertEquals("1234", BaseStringUtils.mergeSave(s1, s2));
        s2 = "1#34";
        Assert.assertEquals("1234", BaseStringUtils.mergeSave(s1, s2));
        s1 = "****";
        Assert.assertEquals("1#34", BaseStringUtils.mergeSave(s1, s2));
        s1 = "*******";
        Assert.assertEquals("1#34", BaseStringUtils.mergeSave(s1, s2));
        s1 = "*******###";
        Assert.assertEquals("1#34", BaseStringUtils.mergeSave(s1, s2));
        s1 = "*******###&&";
        Assert.assertEquals("1#34", BaseStringUtils.mergeSave(s1, s2));
        s1 = null;
        Assert.assertEquals("1#34", BaseStringUtils.mergeSave(s1, s2));
        s1 = "4567";
        Assert.assertEquals("4567", BaseStringUtils.mergeSave(s1, s2));
    }

    @Test
    public void testMerge() {
        String s1 = "1234";
        String s2 = "1234";
        Assert.assertEquals("1234", BaseStringUtils.merge(s1, s2));
        s1 = "12*4";
        Assert.assertEquals("1234", BaseStringUtils.merge(s1, s2));
        s2 = "1#34";
        Assert.assertEquals("1234", BaseStringUtils.merge(s1, s2));
        s1 = "****";
        Assert.assertEquals("1#34", BaseStringUtils.merge(s1, s2));
        s1 = "*******";
        Assert.assertEquals("1#34", BaseStringUtils.merge(s1, s2));
        s1 = "*******###";
        Assert.assertEquals("1#34", BaseStringUtils.merge(s1, s2));
        s1 = "*******###&&";
        Assert.assertEquals(null, BaseStringUtils.merge(s1, s2));
        s1 = null;
        Assert.assertEquals("1#34", BaseStringUtils.merge(s1, s2));
        s1 = "4567";
        Assert.assertEquals(null, BaseStringUtils.merge(s1, s2));
    }

    @Test
    public void testContains() {
        Assert.assertTrue(BaseStringUtils.contains("123", "1", null));
        Assert.assertTrue(BaseStringUtils.contains("123", "2", null));
        Assert.assertTrue(BaseStringUtils.contains("123", "3", null));
        Assert.assertTrue(BaseStringUtils.contains("123", "1", "*"));
        Assert.assertTrue(BaseStringUtils.contains("123", "2", "*#"));
        Assert.assertTrue(BaseStringUtils.contains("123", "3", "#"));
        Assert.assertTrue(BaseStringUtils.contains("123", "1*", "*"));
        Assert.assertTrue(BaseStringUtils.contains("123", "2*", "*#"));
        Assert.assertTrue(BaseStringUtils.contains("123", "#2", "*#"));
        Assert.assertTrue(BaseStringUtils.contains("123", "#3", "#"));
        Assert.assertTrue(BaseStringUtils.contains("123", "*2*", "*"));
        Assert.assertFalse(BaseStringUtils.contains("123", "*2#", "*"));
        Assert.assertFalse(BaseStringUtils.contains("123", "32", "*"));
    }

    @Test
    public void testCollapse() {
        Assert.assertEquals("w y", BaseStringUtils.collapseWhiteSpace("w     y"));
        Assert.assertEquals("w y", BaseStringUtils.collapseWhiteSpace("w y"));
        Assert.assertEquals("wy", BaseStringUtils.collapseWhiteSpace("wy"));
        Assert.assertEquals("", BaseStringUtils.collapseWhiteSpace(""));
        Assert.assertEquals(null, BaseStringUtils.collapseWhiteSpace(null));
    }

    @Test
    public void testUnderScoreToCamel() {
        Assert.assertEquals("testHello", BaseStringUtils.underScoreToCamel("test_hello", false));
        Assert.assertEquals("TestHello", BaseStringUtils.underScoreToCamel("test_hello", true));
        Assert.assertEquals("testHello", BaseStringUtils.underScoreToCamel("testHello", false));
        Assert.assertEquals("TestHello", BaseStringUtils.underScoreToCamel("testHello", true));
        Assert.assertEquals("testHello", BaseStringUtils.underScoreToCamel("TEST_HELLO", false));
        Assert.assertEquals("TestHello", BaseStringUtils.underScoreToCamel("TEST_HELLO", true));
        Assert.assertEquals("TestHeLlo", BaseStringUtils.underScoreToCamel("TEsT_HeLLO", true));
        Assert.assertEquals("TestHello", BaseStringUtils.underScoreToCamel("test_hello_0", true));
    }
}
