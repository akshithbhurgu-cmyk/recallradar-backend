package in.recallradar;
import java.util.List;
record RecallAssistantResponse(String mode,String summary,List<String> recommendations,List<String> evidenceUsed,String humanConfirmationRule){}
