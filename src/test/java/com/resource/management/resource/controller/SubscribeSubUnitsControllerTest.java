package com.resource.management.resource.controller;

import com.resource.management.api.resources.crud.notifications.InitialSubUnitsNotification;
import com.resource.management.resource.model.Equipment;
import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitMapper;
import com.resource.management.resource.model.SubUnitsRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    private SubUnitsRepository repository;

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
                   equalTo(SubUnitMapper.toApi(subUnitsList)));
    }

    private List<SubUnit> prepareSubUnitsInRepository() {
        final List<SubUnit> subUnitsList = new ArrayList<>();
        subUnitsList.add(new SubUnit("CJ", Arrays.asList(resource()), Arrays.asList(equipment()), Instant.now().toString(), null));
        when(this.repository.findAll()).thenReturn(subUnitsList);
        return subUnitsList;
    }

    private Resource resource() {
        Resource resource = new Resource();
        resource.setType(ResourceType.EQUIPMENT);
        return resource;
    }

    private Equipment equipment() {
        Equipment equipment = new Equipment();
        equipment.setResourceType(ResourceType.EQUIPMENT);
        Random r = new Random();
        r.nextInt();
        equipment.setUnusable(r.nextInt());
        equipment.setUnusable(r.nextInt());
        equipment.setReserves(r.nextInt());
        equipment.setEquipmentType("Hatch");
        return equipment;
    }
}