package com.company;

public class TestTwoWayCommand2Handler implements ITwoWayCommandHandler<TestTwoWayCommand2, TwoWayResult> {
    @Override
    public TwoWayResult Handle(TestTwoWayCommand2 command) {
        return new TwoWayResult("Hossein", "Saneifar");
    }
}
