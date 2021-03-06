package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.GestureStateMachine.PointerContext.MoveMethod;
import com.eltechs.axs.GuestAppActionAdapters.MouseMoveAdapter;
import com.eltechs.axs.TouchEventAdapter;
import com.eltechs.axs.finiteStateMachine.FSMEvent;
import com.eltechs.axs.helpers.Assert;
import java.util.List;

public class GestureState1FingerMoveToMouseMove extends AbstractGestureFSMState implements TouchEventAdapter {
    public static FSMEvent GESTURE_COMPLETED = new FSMEvent() {
    };
    private Finger f;
    private final MouseMoveAdapter moveAdapter;
    private PointerContext pointerContext;

    public void notifyMovedIn(Finger finger, List<Finger> list) {
    }

    public void notifyTouched(Finger finger, List<Finger> list) {
    }

    public GestureState1FingerMoveToMouseMove(GestureContext gestureContext, PointerContext pointerContext2, MouseMoveAdapter mouseMoveAdapter) {
        super(gestureContext);
        this.pointerContext = pointerContext2;
        this.moveAdapter = mouseMoveAdapter;
    }

    public void notifyBecomeActive() {
        getContext().getFingerEventsSource().addListener(this);
        boolean z = true;
        if (getContext().getFingers().size() != 1) {
            z = false;
        }
        Assert.state(z);
        this.f = (Finger) getContext().getFingers().get(0);
        this.moveAdapter.prepareMoving(this.f.getXWhenFirstTouched(), this.f.getYWhenFirstTouched());
    }

    public void notifyBecomeInactive() {
        this.f = null;
        getContext().getFingerEventsSource().removeListener(this);
    }

    public void notifyMoved(Finger finger, List<Finger> list) {
        if (finger == this.f) {
            this.moveAdapter.moveTo(finger.getX(), finger.getY());
            this.pointerContext.setLastMoveMethod(MoveMethod.AIM);
        }
    }

    public void notifyReleased(Finger finger, List<Finger> list) {
        if (list.isEmpty()) {
            sendEvent(GESTURE_COMPLETED);
        }
    }

    public void notifyMovedOut(Finger finger, List<Finger> list) {
        if (list.isEmpty()) {
            sendEvent(GESTURE_COMPLETED);
        }
    }
}
