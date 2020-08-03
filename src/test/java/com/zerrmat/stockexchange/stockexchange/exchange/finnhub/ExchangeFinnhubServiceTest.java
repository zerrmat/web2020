package com.zerrmat.stockexchange.stockexchange.exchange.finnhub;

public class ExchangeFinnhubServiceTest {
    /*
    @Mock
    private ExchangeFinnhubRepository exchangeFinnhubRepository;
    @Mock
    private ExchangeFinnhubConverter exchangeFinnhubConverter;

    private ExchangeFinnhubService exchangeFinnhubService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        exchangeFinnhubService = new ExchangeFinnhubService(exchangeFinnhubRepository,
            exchangeFinnhubConverter);
    }

    @Test
    public void shouldSaveFinnhubResponse() throws JsonProcessingException {
        // given
        ExchangeModel model1 = ExchangeModel.builder()
            .code("WA")
            .currency("PLN")
            .name("WARSAW STOCK EXCHANGE/EQUITIES/MAIN MARKET")
            .build();
        ExchangeModel model2 = ExchangeModel.builder()
            .code("US")
            .currency("USD")
            .name("US exchanges")
            .build();
        List<ExchangeModel> modelList = Arrays.asList(model1, model2);

        List<ExchangeFinnhubResponse> responses = new ObjectMapper().readValue(
            generateRequestBody("ExchangeFinnhubRequest.json"),
            new TypeReference<>(){});
        ExchangeFinnhubRequest exchangeFinnhubRequest = new ExchangeFinnhubRequest();
        exchangeFinnhubRequest.setElements(responses);

        Mockito.when(exchangeFinnhubConverter.convertAllToEntity(any(List.class)))
            .thenReturn(modelList);
        Mockito.when(exchangeFinnhubRepository.saveAll(modelList)).thenReturn(modelList);

        // when
        boolean isSuccess = exchangeFinnhubService.save(exchangeFinnhubRequest);

        // then
        Assertions.assertThat(isSuccess).isTrue();
    }

    private String generateRequestBody(String jsonName) {
        String requestBody = "";
        try {
            requestBody = StreamUtils.copyToString(
                new ClassPathResource(jsonName).getInputStream(),
                Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
*/
}
