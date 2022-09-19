package net.cybercake.cyberapi.common.basic;

import com.google.common.base.Preconditions;

import java.io.Serializable;

public class Pair<A, B> implements Serializable {

    private A item1;
    private B item2;

    public Pair(A item1, B item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public void setPair(A item1, B item2) { setFirstItem(item1); setSecondItem(item2); }
    public void setFirstItem(A item1) { this.item1 = item1; }
    public void setSecondItem(B item2) { this.item2 = item2; }

    public A getFirstItem() { return this.item1; }
    public B getSecondItem() { return this.item2; }

    public boolean isFirstItemSet() { return this.item1 != null; }
    public boolean isSecondItemSet() { return this.item2 != null; }

    public boolean areSameType() {
        Preconditions.checkArgument(this.item1 != null, "item1 cannot be null");
        Preconditions.checkArgument(this.item2 != null, "item2 cannot be null");
        return this.item1.getClass() == this.item2.getClass();
    }

    @Override
    public String toString() {
        Preconditions.checkArgument(this.item1 != null, "item1 cannot be null");
        Preconditions.checkArgument(this.item2 != null, "item2 cannot be null");
        return this.getClass().getSimpleName() + "{" + item1.getClass().getCanonicalName() + " " + item1.toString() + ", " + item2.getClass().getCanonicalName() + " " + item2.toString() + "}";
    }

}