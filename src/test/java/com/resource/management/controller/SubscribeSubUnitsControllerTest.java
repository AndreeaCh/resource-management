package com.resource.management.controller;

import com.resource.management.api.crud.notifications.InitialSubUnitsNotification;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscribeSubUnitsControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private SubscribeSubUnitsController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleSubscribeRequest_sut_returnsSubUnitsList() {
        //given
        final List<SubUnit> subUnitsList = prepareSubUnitsInRepository();

        //when
        final InitialSubUnitsNotification response = this.sut.handleSubscribeMessage();

        //then
        assertThat("Expected response to contain the list of sub-units.", response.getSubUnitsList(),
                equalTo(subUnitsList));
    }

    private List<SubUnit> prepareSubUnitsInRepository() {
        final List<SubUnit> subUnitsList = new ArrayList<>();
        subUnitsList.add(new SubUnit("CJ", Collections.emptyList(), Instant.now().toString(), null, true));
        when(this.subUnitsRepository.findAll()).thenReturn(subUnitsList);
        return subUnitsList;
    }
}
