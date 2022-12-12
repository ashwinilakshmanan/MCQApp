package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Option;
import com.mycompany.myapp.repository.OptionRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionResourceIT {

    private static final String DEFAULT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CORRECT = false;
    private static final Boolean UPDATED_IS_CORRECT = true;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionMockMvc;

    private Option option;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity(EntityManager em) {
        Option option = new Option().option(DEFAULT_OPTION).isCorrect(DEFAULT_IS_CORRECT);
        return option;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity(EntityManager em) {
        Option option = new Option().option(UPDATED_OPTION).isCorrect(UPDATED_IS_CORRECT);
        return option;
    }

    @BeforeEach
    public void initTest() {
        option = createEntity(em);
    }

    @Test
    @Transactional
    void createOption() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().size();
        // Create the Option
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isCreated());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate + 1);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getOption()).isEqualTo(DEFAULT_OPTION);
        assertThat(testOption.getIsCorrect()).isEqualTo(DEFAULT_IS_CORRECT);
    }

    @Test
    @Transactional
    void createOptionWithExistingId() throws Exception {
        // Create the Option with an existing ID
        option.setId(1L);

        int databaseSizeBeforeCreate = optionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOptions() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION)))
            .andExpect(jsonPath("$.[*].isCorrect").value(hasItem(DEFAULT_IS_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    void getOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.option").value(DEFAULT_OPTION))
            .andExpect(jsonPath("$.isCorrect").value(DEFAULT_IS_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).get();
        // Disconnect from session so that the updates on updatedOption are not directly saved in db
        em.detach(updatedOption);
        updatedOption.option(UPDATED_OPTION).isCorrect(UPDATED_IS_CORRECT);

        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testOption.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
    }

    @Test
    @Transactional
    void putNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, option.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.isCorrect(UPDATED_IS_CORRECT);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getOption()).isEqualTo(DEFAULT_OPTION);
        assertThat(testOption.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
    }

    @Test
    @Transactional
    void fullUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.option(UPDATED_OPTION).isCorrect(UPDATED_IS_CORRECT);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testOption.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
    }

    @Test
    @Transactional
    void patchNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, option.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeDelete = optionRepository.findAll().size();

        // Delete the option
        restOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, option.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
