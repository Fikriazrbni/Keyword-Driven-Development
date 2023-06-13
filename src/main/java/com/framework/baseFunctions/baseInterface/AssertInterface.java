package com.framework.baseFunctions.baseInterface;

/**
 * Interface for AssertClass
 */
public interface AssertInterface {
    void assertWebElementNotExist();
    void assertWebElementExist();

    void assertWebElementTextTrue();
    void assertWebElementTextFalse();

    void assertWebElementTextContainsTrue();
    void assertWebElementTextContainsFalse();

//    void assertObjectNull(Object strObject, String strDescription);
//    void assertObjectNotNull(Object strObject, String strDescription);
//
//    void assertSame(Object strObject, Object strObjectCompare, String strDescription);
//    void assertNotSame(Object strObject, Object strObjectCompare, String strDescription);

}
