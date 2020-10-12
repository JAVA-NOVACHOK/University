package ua.com.nikiforov.services.room;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.room.RoomDAO;

@TestInstance(Lifecycle.PER_CLASS)
@SpringJUnitConfig(UniversityConfig.class)
class RoomServiceImplTest {

    @Mock
    private RoomDAO roomDAO;
    
    @InjectMocks
    private RoomServiceImpl roomService;

//    @BeforeAll
//    void init() {
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    void test() {
//        when(roomDAO.addRoom(12)).thenReturn(true);
        assertEquals(roomService.addRoom(12));
//        assertEqualse(true, roomService.addRoom(12));
    }

}
