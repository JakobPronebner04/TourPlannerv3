import static org.junit.jupiter.api.Assertions.assertEquals;

public class TourServiceTest {

        /*private TourService tourService;
        private EventManager eventManager;
        private StateRepository stateRepository;

        @BeforeEach
        void setUp() {
            eventManager = mock(EventManager.class);
            stateRepository = mock(StateRepository.class);

            tourService = new TourService(eventManager, stateRepository);

            Tour initialTour = new Tour();
            initialTour.setTourName("Tour 1");
            initialTour.setTourDescription("Tour Description");
            initialTour.setTourStart("Tour Start");
            initialTour.setTourDestination("Tour Destination");
            initialTour.setTourTransportType("Bicycle");

            when(stateRepository.getSelectedTour()).thenReturn(initialTour);
        }

        @Test
        void Change_ShouldSuccessfullyEditTour() throws IllegalAccessException {
            Tour updatedTour = new Tour();
            updatedTour.setTourName("Tour 2");
            updatedTour.setTourDescription("Tour Description");
            updatedTour.setTourStart("Tour Start");
            updatedTour.setTourDestination("Tour Destination");
            updatedTour.setTourTransportType("Car");

            tourService.getTours().add(stateRepository.getSelectedTour());

            tourService.edit(updatedTour);

            assertEquals(updatedTour, tourService.getTours().getFirst());

            verify(eventManager).publish(Events.TOURS_EDITED, "UPDATE_TOURLOGS");
        }*/

}
