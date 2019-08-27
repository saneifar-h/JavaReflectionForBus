package com.company;

public interface ITwoWayCommandHandler<T1 extends ITwoWayCommand<T2>,T2>  {
    T2 Handle(T1 command);
}
