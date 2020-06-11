package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateTrackerTest {

    @Test
    public void showTopCard() {
        StateTracker stateTracker = new StateTracker();
        stateTracker.showTopCard();
    }
}