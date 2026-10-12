package in.recallradar;
import jakarta.validation.constraints.NotBlank;
record SampleSeedRequest(@NotBlank String setupCode){}
record SampleSeedResponse(String organizationName,int locations,int suppliers,int ingredients,int lots,int recipes,int productionBatches,int incidents,int recalls){ }
