package com.resource.management.controller;

import com.resource.management.api.DeleteSubUnitRequest;
import com.resource.management.api.SubUnitDeletedNotification;
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
public class DeleteSubUnitControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private DeleteSubUnitController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleAddSubUnitRequest_sut_publishesNewSubUnit() {
        //given
        DeleteSubUnitRequest request = new DeleteSubUnitRequest("CJ");

        //when
        SubUnitDeletedNotification notification = this.sut.handle(request);

        //then
        assertThat("Expected notification to contain the deleted sub-unit deletedSubUnitName.", notification.getDeletedSubUnitName(),
                   equalTo(request.getName()));
    }
}