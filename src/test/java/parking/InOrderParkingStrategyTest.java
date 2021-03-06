package parking;

import mocking.MessageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {InOrderParkingStrategy.class})
public class InOrderParkingStrategyTest {

	@Test
    public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() throws InvocationTargetException, IllegalAccessException {
        //given
        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        ParkingLot parkingLot = mock(ParkingLot.class);
        Car car = mock(Car.class);
        when(parkingLot.getName()).thenReturn("parkingLot");
        when(car.getName()).thenReturn("Car");
        //when
        Method method = PowerMockito.method(InOrderParkingStrategy.class, "createReceipt", ParkingLot.class, Car.class);
        Receipt receipt = (Receipt)method.invoke(inOrderParkingStrategy, parkingLot,car);
        //then
        assertEquals("Car",receipt.getCarName());
        assertEquals("parkingLot",receipt.getParkingLotName());
    }

    @Test
    public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() throws InvocationTargetException, IllegalAccessException {

        //given
        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        Car car = mock(Car.class);
        when(car.getName()).thenReturn("car");
        //when
        Method method = PowerMockito.method(InOrderParkingStrategy.class,"createNoSpaceReceipt",Car.class);
        Receipt receipt = (Receipt)method.invoke(inOrderParkingStrategy, car);
        //then
        assertEquals("car",receipt.getCarName());
        assertEquals("No Parking Lot",receipt.getParkingLotName());

    }

    @Test
    public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt(){
        //given
        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Car car = mock(Car.class);
        //when
        inOrderParkingStrategy.park(null,car);
        //then
        verify(inOrderParkingStrategy,times(1)).createNoSpaceReceipt(car);

    }

    @Test
    public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt(){

        //given
        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Car car = mock(Car.class);
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.isFull()).thenReturn(false);
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(parkingLot);
        //when
        inOrderParkingStrategy.park(parkingLots,car);
        //then
        verify(inOrderParkingStrategy,times(1)).createReceipt(parkingLot,car);
    }

    @Test
    public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt(){

        //given
        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Car car = mock(Car.class);
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.isFull()).thenReturn(true);
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(parkingLot);
        //when
        inOrderParkingStrategy.park(parkingLots,car);
        //then
        verify(inOrderParkingStrategy,times(1)).createNoSpaceReceipt(car);

    }

    @Test
    public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot(){

        /* Exercise 3: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for multiple parking lot situation */
        //given
        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Car car = mock(Car.class);
        ParkingLot parkingLot = mock(ParkingLot.class);
        ParkingLot parkingLot1 = mock(ParkingLot.class);
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(parkingLot);
        parkingLots.add(parkingLot1);
        when(parkingLot.isFull()).thenReturn(true);
        when(parkingLot1.isFull()).thenReturn(false);
        //when
        inOrderParkingStrategy.park(parkingLots,car);
        //then
        verify(inOrderParkingStrategy,times(1)).createReceipt(parkingLot1,car);
    }


}
