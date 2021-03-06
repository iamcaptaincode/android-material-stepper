package com.stepstone.stepper.sample;

import androidx.test.filters.LargeTest;

import com.stepstone.stepper.internal.widget.StepTabStateMatcher;
import com.stepstone.stepper.sample.test.action.SpoonScreenshotAction;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.stepstone.stepper.sample.test.matcher.CommonMatchers.checkCompleteButtonShown;
import static com.stepstone.stepper.sample.test.matcher.CommonMatchers.checkCurrentStepIs;
import static com.stepstone.stepper.sample.test.matcher.CommonMatchers.checkTabState;
import static com.stepstone.stepper.test.StepperNavigationActions.clickBack;
import static com.stepstone.stepper.test.StepperNavigationActions.clickComplete;
import static com.stepstone.stepper.test.StepperNavigationActions.clickNext;
import static org.hamcrest.Matchers.allOf;

/**
 * Performs tests on a tabbed stepper i.e. the one with {@code ms_stepperType="tabs"}.
 * This also tests if the errors are shown in the tabs.
 *
 * @author Piotr Zawadzki
 */
@LargeTest
public class ShowErrorTabActivityTest extends AbstractActivityTest<ShowErrorTabActivity> {

    @Test
    public void shouldStayOnTheFirstStepWhenVerificationFailsAndShowError() {
        //when
        onView(withId(R.id.stepperLayout)).perform(clickNext());

        //then
        checkCurrentStepIs(0);
        checkTabState(0, StepTabStateMatcher.TabState.WARNING);
        checkTabState(1, StepTabStateMatcher.TabState.INACTIVE);
        checkTabState(2, StepTabStateMatcher.TabState.INACTIVE);
        SpoonScreenshotAction.perform(getScreenshotTag(1, "Verification failure test"));
    }

    @Test
    public void shouldGoToTheNextStepWhenVerificationSucceeds() {
        //given
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickNext());

        //then
        checkCurrentStepIs(1);
        SpoonScreenshotAction.perform(getScreenshotTag(2, "Verification success test"));
    }

    @Test
    public void shouldShowCompleteButtonOnTheLastStep() {
        //given
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickNext());

        //then
        checkCurrentStepIs(2);
        checkCompleteButtonShown();
        SpoonScreenshotAction.perform(getScreenshotTag(3, "Last step test"));
    }

    @Test
    public void shouldGoToTheNextStepAndClearWarningWhenStepVerificationSucceeds() {
        //given
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickNext());

        //then
        checkCurrentStepIs(1);
        checkTabState(0, StepTabStateMatcher.TabState.DONE);
        checkTabState(1, StepTabStateMatcher.TabState.ACTIVE);
        checkTabState(2, StepTabStateMatcher.TabState.INACTIVE);
        SpoonScreenshotAction.perform(getScreenshotTag(4, "Clear warning on success test"));
    }

    @Test
    public void shouldClearWarningWhenGoingBackToPreviousStep() {
        //given
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(withId(R.id.stepperLayout)).perform(clickNext());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickBack());

        //then
        checkCurrentStepIs(0);
        checkTabState(0, StepTabStateMatcher.TabState.ACTIVE);
        checkTabState(1, StepTabStateMatcher.TabState.INACTIVE);
        checkTabState(2, StepTabStateMatcher.TabState.INACTIVE);
        SpoonScreenshotAction.perform(getScreenshotTag(5, "Clear warning on Back test"));
    }

    @Test
    public void shouldCompleteStepperFlow() {
        //given
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickComplete());

        //then
        checkCurrentStepIs(2);
        checkTabState(0, StepTabStateMatcher.TabState.DONE);
        checkTabState(1, StepTabStateMatcher.TabState.DONE);
        checkTabState(2, StepTabStateMatcher.TabState.ACTIVE);
        SpoonScreenshotAction.perform(getScreenshotTag(6, "Complettion test"));
    }

    @Test
    public void shouldShowErrorOnLastStep() {
        //given
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickComplete());

        //then
        checkCurrentStepIs(2);
        checkTabState(0, StepTabStateMatcher.TabState.DONE);
        checkTabState(1, StepTabStateMatcher.TabState.DONE);
        checkTabState(2, StepTabStateMatcher.TabState.WARNING);
        SpoonScreenshotAction.perform(getScreenshotTag(7, "Last step warning test"));
    }

    @Test
    public void shouldClearWarningOnLastStep() {
        //given
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());
        onView(withId(R.id.stepperLayout)).perform(clickNext());
        onView(withId(R.id.stepperLayout)).perform(clickComplete());
        onView(allOf(withId(R.id.button), isCompletelyDisplayed())).perform(doubleClick());

        //when
        onView(withId(R.id.stepperLayout)).perform(clickComplete());

        //then
        checkCurrentStepIs(2);
        checkTabState(0, StepTabStateMatcher.TabState.DONE);
        checkTabState(1, StepTabStateMatcher.TabState.DONE);
        checkTabState(2, StepTabStateMatcher.TabState.ACTIVE);
        SpoonScreenshotAction.perform(getScreenshotTag(8, "Clear warning on last step test"));
    }

}
