package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.interfaces.services.WorkdayService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class WorkdayServiceImplTest
{
    @InjectMocks
    private WorkdayService workdayService;
    @Mock
    private WorkdayDao workdayDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateWorkday()
    {
        //todo
    }
}
