package org.gdo.voucherio.controller;

import org.gdo.voucherio.voucher.controller.VoucherController;
import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.gdo.voucherio.voucher.exception.VoucherAlreadyExistsException;
import org.gdo.voucherio.voucher.model.VoucherRequest;
import org.gdo.voucherio.voucher.model.VoucherResponse;
import org.gdo.voucherio.voucher.service.VoucherSearchCriteria;
import org.gdo.voucherio.voucher.service.VoucherService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(VoucherController.class)
public class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VoucherService voucherService;

    @Test
    public void testGetVoucherByCode() throws Exception {

        // test object
        VoucherResponse voucher = new VoucherResponse("LEU123", "1h", false);

        // conditions
        when(voucherService.findByCode("LEU123")).thenReturn(voucher);

        this.mockMvc.perform(get("/vouchers/{code}", "LEU123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("LEU123"))
                .andExpect(jsonPath("$.duration").value("1h"))
                .andExpect(jsonPath("$.used").value(false));

    }

    @Test
    public void testGetVoucherByCodeNotFound() throws Exception {

        // test data
        final String voucherCode = "LEU123";

        // conditions
        when(voucherService.findByCode(voucherCode)).thenThrow(NoVoucherExistsException.class);

        this.mockMvc.perform(get("/vouchers/{code}", voucherCode))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllUnusedVouchers() throws Exception {

        // test data
        VoucherResponse expectedVoucher1 = new VoucherResponse("LEU123", "1h", false);
        VoucherResponse expectedVoucher2 = new VoucherResponse("LEU456", "2h", false);
        VoucherResponse expectedVoucher3 = new VoucherResponse("LEU789", "4h", false);

        List<VoucherResponse> expectedVouchers = new ArrayList<>();
        expectedVouchers.add(expectedVoucher1);
        expectedVouchers.add(expectedVoucher2);
        expectedVouchers.add(expectedVoucher3);
        
        // conditions
        when(voucherService.findAll(new VoucherSearchCriteria(null,null))).thenReturn(expectedVouchers);

        this.mockMvc.perform(get("/vouchers"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.length()").value(expectedVouchers.size()))
            .andExpect(jsonPath("$[0].code").value(expectedVoucher1.getCode()))
            .andExpect(jsonPath("$[1].code").value(expectedVoucher2.getCode()))
            .andExpect(jsonPath("$[2].code").value(expectedVoucher3.getCode()));


    }

    @Test
    public void testGetOnlyUnusedVouchers() throws Exception {

        // test data
        VoucherResponse expectedVoucher1 = new VoucherResponse("LEU123", "1h", false);
        VoucherResponse expectedVoucher3 = new VoucherResponse("LEU789", "4h", false);

        List<VoucherResponse> expectedVouchers = new ArrayList<>();
        expectedVouchers.add(expectedVoucher1);
        expectedVouchers.add(expectedVoucher3);
        
        // conditions
        when(voucherService.findAll(new VoucherSearchCriteria(null,false))).thenReturn(expectedVouchers);

        this.mockMvc.perform(get("/vouchers?includeUsed={includeUsed}", false))

            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.length()").value(expectedVouchers.size()))
            .andExpect(jsonPath("$[0].code").value(expectedVoucher1.getCode()))
            .andExpect(jsonPath("$[1].code").value(expectedVoucher3.getCode()));
    }

    @Test
    public void testGetAllVouchers() throws Exception {

        // test data
        VoucherResponse expectedVoucher1 = new VoucherResponse("LEU123", "1h", false);
        VoucherResponse expectedVoucher2 = new VoucherResponse("LEU456", "2h", true);
        VoucherResponse expectedVoucher3 = new VoucherResponse("LEU789", "4h", false);

        List<VoucherResponse> expectedVouchers = new ArrayList<>();
        expectedVouchers.add(expectedVoucher1);
        expectedVouchers.add(expectedVoucher2);
        expectedVouchers.add(expectedVoucher3);
        
        // conditions
        when(voucherService.findAll(new VoucherSearchCriteria(null,true))).thenReturn(expectedVouchers);

        this.mockMvc.perform(get("/vouchers?includeUsed={includeUsed}", true))

            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.length()").value(expectedVouchers.size()))
            .andExpect(jsonPath("$[0].code").value(expectedVoucher1.getCode()))
            .andExpect(jsonPath("$[1].code").value(expectedVoucher2.getCode()))
            .andExpect(jsonPath("$[2].code").value(expectedVoucher3.getCode()));
    }

    @Test
    public void testGetAllUnusedVouchersNothingFound() throws Exception {

        // conditions
        when(voucherService.findAll(new VoucherSearchCriteria(null,null))).thenThrow(new NoVoucherExistsException("no vouchers found"));

        this.mockMvc.perform(get("/vouchers"))
            .andExpect(status().isNotFound());

    }

    @Test
    public void testAddVoucher() throws Exception {

        // test object
        VoucherResponse expectedVoucher = new VoucherResponse("LEU123", "1h", false);

        // conditions
        when(voucherService.addVoucher(any(VoucherRequest.class))).thenReturn(expectedVoucher);

        this.mockMvc.perform(post("/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"LEU123\",\"duration\": \"1h\"}"))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("LEU123"))
                .andExpect(jsonPath("$.duration").value("1h"))
                .andExpect(jsonPath("$.used").value(false));
    }

    @Test
    public void testAddVoucherInvalidDuration() throws Exception {

        // conditions
        // This one is too specific, its testing SERVICE logic not CONTROLLER logic!
        // when(voucherService.addVoucher(argThat(voucher ->
        // !VoucherDuration.isValid(voucher.getDuration()))))
        // .thenThrow(IllegalArgumentException.class);
        when(voucherService.addVoucher(any())).thenThrow(IllegalArgumentException.class);

        this.mockMvc.perform(post("/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"LEU123\",\"duration\": \"11h\"}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testAddVoucherAlreadyExists() throws Exception {

        // conditions
        when(voucherService.addVoucher(any())).thenThrow(VoucherAlreadyExistsException.class);

        this.mockMvc.perform(post("/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"LEU123\",\"duration\": \"11h\"}"))
                .andExpect(status().isConflict());

    }

    @Test
    public void testUseVoucher() throws Exception {

        final String voucherCode = "LEU123";

        this.mockMvc.perform(put("/vouchers/{code}", voucherCode))
                .andExpect(status().isOk());

        verify(voucherService).useVoucher(voucherCode);
    }

    @Test
    public void testUseVoucherDoesNotExist() throws Exception {

        final String voucherCode = "LEU123";

        // conditions
        doThrow(new NoVoucherExistsException("voucher does not exist")).when(voucherService).useVoucher(voucherCode);

        this.mockMvc.perform(put("/vouchers/{code}", voucherCode))
                .andExpect(status().isNotFound());

        // not really needed - the fact there's an exception means the method has
        // certainly been called
        verify(voucherService).useVoucher(voucherCode);
    }

    @Test
    public void testDeleteVoucher() throws Exception {

        final String voucherCode = "LEU123";

        this.mockMvc.perform(delete("/vouchers/{code}", voucherCode))
                .andExpect(status().isOk());

        verify(voucherService).delete(voucherCode);

    }

    @Test
    public void testDeleteVoucherDoesNotExist() throws Exception {

        final String voucherCode = "LEU123";

        // conditions
        doThrow(new NoVoucherExistsException("voucher does not exist")).when(voucherService).delete(voucherCode);

        this.mockMvc.perform(delete("/vouchers/{code}", voucherCode))
                .andExpect(status().isNotFound());

    }
}
