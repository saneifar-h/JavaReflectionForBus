package com.company;

public class TestTwoWayCommandHandler implements ITwoWayCommandHandler<TestTwoWayCommand, String> {
    @Override
    public String Handle(TestTwoWayCommand command) {
        return "Hello : " + command.FirstName + " " + command.LastName;
    }
}

