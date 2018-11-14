package com.resource.management.controller;

import com.resource.management.api.SubscribeSubUnitsResponse;
import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscribeSubUnitControllerTest {
    @MockBean
    private SubUnitRepository subUnitRepository;

    @Autowired
    private SubscribeSubUnitController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(sut, notNullValue());
    }

    @Test
    public void handleSubscribeRequest_sut_returnsSubUnitsList() {
        //given
        List<SubUnit> subUnitsList = prepareSubUnitsInRepository();

        //when
        SubscribeSubUnitsResponse subscribeSubUnitsResponse = sut.handleSubscribeMessage();

        //then
        assertThat("Expected response to contain the list of sub-units.",
                subscribeSubUnitsResponse.getSubUnitsList(),
                equalTo(subUnitsList));
    }

    private List<SubUnit> prepareSubUnitsInRepository() {
        List<SubUnit> subUnitsList = new ArrayList<>();
        subUnitsList.add(new SubUnit("CJ", Collections.emptyList()));
        when(subUnitRepository.findAll()).thenReturn(subUnitsList);
        return subUnitsList;
    }
}