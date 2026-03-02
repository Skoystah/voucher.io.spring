package org.gdo.voucherio.controller;

import org.gdo.voucherio.voucher.controller.VoucherController;
import org.gdo.voucherio.voucher.exception.NoVoucherExistsException;
import org.gdo.voucherio.voucher.exception.VoucherAlreadyExistsException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
