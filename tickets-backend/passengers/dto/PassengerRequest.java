public class PassengerRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    @NotBlank
    private String documentNumber;
    private LocalDate birthDate;
}