package in.recallradar;
import jakarta.validation.constraints.NotBlank;
record DemoSeedRequest(@NotBlank String setupCode){}
record DemoSeedResponse(String organizationName,int locations,int suppliers,int ingredients,int lots,int recipes,int productionBatches,int incidents,int recalls){ }
