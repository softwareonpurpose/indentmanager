package com.softwareonpurpose.indentmanager;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class IndentManagerTest {
    @DataProvider
    public static Object[][] nonPositiveSpacesPerLevel() {
        return new Object[][]{{0}, {-5}};
    }

    @DataProvider
    public static Object[][] multiplePositiveSpacesPerLevel() {
        return new Object[][]{{2, "  "}, {3, "   "}, {5, "     "}};
    }

    @DataProvider
    public static Object[][] multipleIndentationsOfMultipleSpacesPerLevel() {
        return new Object[][]{
                {2, 2, "    "},
                {2, 3, "      "},
                {3, 2, "      "},
                {2, 5, "          "},
                {5, 3, "               "},
                {3, 8, "                        "}};
    }

    @DataProvider
    public static Object[][] positiveSpacesPerLevel() {
        return new Object[][]{{1}, {2}, {3}, {5}, {8}};
    }

    @DataProvider
    public static Object[][] decrementingScenarios() {
        return new Object[][]{{1, ""}, {2, "  "}, {3, "    "}, {0, ""}};
    }

    @Test
    public void rootLevelOnDefaultInstantiation() {
        String message = "On default instantiation, IndentManager failed to be at root level";
        Assert.assertTrue(IndentManager.getInstance().isAtRootLevel(), message);
    }

    @Test
    public void indentationOnDefaultInstantiation() {
        String message = "On default instantiation, indent failed to be two spaces";
        Assert.assertEquals(IndentManager.getInstance().format("."), ".", message);
    }

    @Test
    public void rootLevelOnInitializedInstantiation() {
        String message = "On initialized instantiation, IndentManager failed to be at root level";
        Assert.assertTrue(IndentManager.getInstance(5).isAtRootLevel(), message);
    }

    @Test
    public void rootLevelAfterLevelIncremented(){
        String message = "After increment, IndentManager was still at root level";
        IndentManager manager = IndentManager.getInstance();
        manager.increment();
        Assert.assertFalse(manager.isAtRootLevel(), message);
    }

    @Test
    public void rootLevelAfterLevelDecrementedToRoot(){
        String message = "After decrementing to zero indentations, IndentManager failed to be at root level";
        IndentManager manager = IndentManager.getInstance();
        manager.increment();
        manager.decrement();
        Assert.assertTrue(manager.isAtRootLevel(), message);
    }

    @Test
    public void indentationOnPositiveInitializedInstantiation() {
        String message = "On initialized instantiation, indent failed to be the initialized number of spaces";
        Assert.assertEquals(IndentManager.getInstance(5).format("."), ".", message);
    }

    @Test(dataProvider = "multiplePositiveSpacesPerLevel")
    public void levelOneIndentationOnPositiveInitializedInstantiation(int spacesPerLevel, String expectedIndentation) {
        String message = String.format("On instantiation initialized to %d, indent failed to be '%s'",
                spacesPerLevel, expectedIndentation);
        IndentManager indentManager = IndentManager.getInstance(spacesPerLevel);
        indentManager.increment();
        Assert.assertEquals(indentManager.format("."), String.format("%s.", expectedIndentation), message);
    }

    @Test(dataProvider = "nonPositiveSpacesPerLevel")
    public void levelOneIndentationOnNonPositiveInitializedInstantiation(int spacesPerLevel) {
        String message = String.format("On instantiation initialized to %d, indent failed to be zero spaces",
                spacesPerLevel);
        IndentManager indentManager = IndentManager.getInstance(spacesPerLevel);
        indentManager.increment();
        Assert.assertEquals(indentManager.format("."), ".", message);
    }

    @Test(dataProvider = "positiveSpacesPerLevel")
    public void nullMessage(int spacesPerLevel) {
        IndentManager indentManager = IndentManager.getInstance(spacesPerLevel);
        indentManager.increment();
        Assert.assertEquals(indentManager.format(null), "", "Null message failed to return empty String");
    }

    @Test(dataProvider = "positiveSpacesPerLevel")
    public void emptyMessage(int spacesPerLevel) {
        IndentManager indentManager = IndentManager.getInstance(spacesPerLevel);
        indentManager.increment();
        Assert.assertEquals(indentManager.format(""), "", "Empty message failed to return empty String");
    }

    @Test(dataProvider = "multiplePositiveSpacesPerLevel")
    public void incrementTwiceOnPositiveInitializedInstantiation(int spacesPerLevel, String oneLevelOfSpaces) {
        final String message = "Message Text";
        String failureMessageFormat = "Instantiated with %d and incremented twice, indentation failed to be '%s'";
        String indentation = String.format("%s%s", oneLevelOfSpaces, oneLevelOfSpaces);
        final String failureMessage = String.format(failureMessageFormat, spacesPerLevel, indentation);
        final IndentManager manager = IndentManager.getInstance(spacesPerLevel);
        manager.increment();
        manager.increment();
        Assert.assertEquals(manager.format(message), String.format("%s%s", indentation, message), failureMessage);
    }

    @Test(dataProvider = "multipleIndentationsOfMultipleSpacesPerLevel")
    public void incrementMultipleTimesOnPositiveInitializedInstantiation(int levels, int spacesPerLevel, String
            indentation) {
        final String message = "Message Text";
        String failureMessageFormat = "Instantiated with %d and incremented %d times, indentation failed to be '%s'";
        final String failureMessage = String.format(failureMessageFormat, spacesPerLevel, levels, indentation);
        final IndentManager manager = IndentManager.getInstance(spacesPerLevel);
        manager.increment(levels);
        Assert.assertEquals(manager.format(message), String.format("%s%s", indentation, message), failureMessage);
    }

    @Test(dataProvider = "decrementingScenarios")
    public void decrementOnce(int levelsToIncrement, String expectedIndentation) {
        final String message = "Message Text";
        final IndentManager manager = IndentManager.getInstance();
        manager.increment(levelsToIncrement);
        manager.decrement();
        String failureMessageFormat = "After incrementing by %d and decrementing once, indentation failed to be '%s'";
        String failureMessage = String.format(failureMessageFormat, levelsToIncrement, expectedIndentation);
        Assert.assertEquals(manager.format(message), String.format("%s%s", expectedIndentation, message),
                failureMessage);
    }
}
