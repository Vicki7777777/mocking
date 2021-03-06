package parking;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class VipParkingStrategyTest {
    @Mock
    CarDao carDao;
    @InjectMocks
    VipParkingStrategy vipParkingStrategy;

	@Test
    public void testPark_givenAVipCarAndAFullParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {
        //given
        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());
        ParkingLot parkingLot = mock(ParkingLot.class);
        Car car = mock(Car.class);
        List<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(parkingLot);
        //when
        doReturn(false).when(parkingLot).isFull();

        Receipt receipt = vipParkingStrategy.park(parkingLots,car);
        //then
        verify(vipParkingStrategy,times(1)).createReceipt(parkingLot,car);

    }

    @Test
    public void testPark_givenCarIsNotVipAndAFullParkingLog_thenGiveNoSpaceReceipt() {

        //given
        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());
        ParkingLot parkingLot = mock(ParkingLot.class);
        Car car = mock(Car.class);
        List<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(parkingLot);
        //when
        doReturn(true).when(parkingLot).isFull();

        Receipt receipt = vipParkingStrategy.park(parkingLots,car);
        //then
        verify(vipParkingStrategy,times(1)).createNoSpaceReceipt(car);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsVipCar_thenReturnTrue(){
        //given
        Car car = mock(Car.class);
        when(car.getName()).thenReturn("A");
        when(carDao.isVip(car.getName())).thenReturn(true);
        //when
        Boolean flag = vipParkingStrategy.isAllowOverPark(car);
        //then
        assertEquals(true,flag);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsVipCar_thenReturnFalse(){

        //given
        Car car = mock(Car.class);
        when(car.getName()).thenReturn("B");
        when(carDao.isVip(car.getName())).thenReturn(true);
        //when
        Boolean flag = vipParkingStrategy.isAllowOverPark(car);
        //then
        assertEquals(false,flag);
    }


    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsNotVipCar_thenReturnFalse(){
        //given
        Car car = mock(Car.class);
        when(car.getName()).thenReturn("A");
        when(carDao.isVip(car.getName())).thenReturn(false);
        //when
        Boolean flag = vipParkingStrategy.isAllowOverPark(car);
        //then
        assertEquals(false,flag);

    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
        //given
        Car car = mock(Car.class);
        when(car.getName()).thenReturn("B");
        when(carDao.isVip(car.getName())).thenReturn(false);
        //when
        Boolean flag = vipParkingStrategy.isAllowOverPark(car);
        //then
        assertEquals(false,flag);
    }


    private Car createMockCar(String carName) {
        Car car = mock(Car.class);
        when(car.getName()).thenReturn(carName);
        return car;
    }
}
