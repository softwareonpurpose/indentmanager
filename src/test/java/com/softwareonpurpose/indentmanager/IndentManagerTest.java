/*
  Copyright 2021 - 2022 Craig A. Stockton
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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
    public static Object[][] decrementOnceScenarios() {
        return new Object[][]{{1, ""}, {2, "  "}, {3, "    "}, {0, ""}};
    }

    @DataProvider
    public static Object[][] decrementLevelsScenarios() {
        return new Object[][]{
                {0},{5},{9}
        };
    }

    @Test
    public void rootLevelOnDefaultInstantiation() {
        String message = "On default instantiation, IndentManager failed to be at root level";
        Assert.assertTrue(IndentManager.getInstance().isAtRootLevel(), message);
    }

    @Test
    public void indentationOnDefaultInstantiation() {
        String message = "On default instantiation, indent failed to be zero spaces";
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

    @Test(dataProvider = "decrementOnceScenarios")
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

    @Test(dataProvider = "decrementLevelsScenarios")
    public void decrementMultipleLevels(int levelsToDecrement){
        final String message = "Message Text";
        String failureMessageFormat = "After incrementing by %d and decrementing by %d, indentation failed to be '%s'";
        int levelsToIncrement = levelsToDecrement + 2;
        String expectedIndentation = "    ";
        String failureMessage = String.format(failureMessageFormat, levelsToIncrement, levelsToDecrement, expectedIndentation);
        final IndentManager manager = IndentManager.getInstance();
        manager.increment(levelsToIncrement);
        manager.decrement(levelsToDecrement);
        Assert.assertEquals(manager.format(message), String.format("%s%s", expectedIndentation, message), failureMessage);
    }
}
