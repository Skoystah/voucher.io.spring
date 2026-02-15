package org.gdo.voucherio.controller;

import org.gdo.voucherio.voucher.controller.VoucherController;
import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.gdo.voucherio.voucher.model.VoucherRequest;
import org.gdo.voucherio.voucher.model.VoucherResponse;
import org.gdo.voucherio.voucher.service.VoucherService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
                .andExpect(jsonPath("$.code").value("LEU123"))
                .andExpect(jsonPath("$.duration").value("1h"))
                .andExpect(jsonPath("$.used").value(false));

    }

    @Test
    public void testGetVoucherByCodeNotFound() throws Exception {

        // conditions
        when(voucherService.findByCode("LEU123")).thenThrow(NoVoucherExistsException.class);

        this.mockMvc.perform(get("/vouchers/{code}", "LEU123"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testAddVoucher() throws Exception {

        // test object
        VoucherResponse voucher = new VoucherResponse("LEU123", "1h", false);

        // conditions
        when(voucherService.addVoucher(any())).thenReturn(voucher);

        this.mockMvc.perform(post("/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"LEU123\",\"duration\": \"1h\"}"))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("LEU123"))
                .andExpect(jsonPath("$.duration").value("1h"))
                .andExpect(jsonPath("$.used").value(false));
    }

}
