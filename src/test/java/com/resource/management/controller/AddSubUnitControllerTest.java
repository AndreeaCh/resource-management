package com.resource.management.controller;

import com.resource.management.SubUnitsTestDataUtils;
import com.resource.management.api.AddSubUnitRequest;
import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.data.SubUnitsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddSubUnitControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private AddSubUnitController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleAddSubUnitRequest_sut_publishesNewSubUnit() {
        //given
        AddSubUnitRequest request = new AddSubUnitRequest(SubUnitsTestDataUtils.loadRandomSubUnit());

        //when
        SubUnitUpdatedNotification notification = this.sut.handle(request);

        //then
        assertThat("Expected notification to contain the added sub-unit.", notification.getSubUnit(),
                equalTo(request.getSubUnit()));
    }
}
