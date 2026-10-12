package in.recallradar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
class SampleDataService {
  private final TenantService tenants; private final InventoryService inventory; private final ProductionService production;
  private final IncidentService incidents; private final RecallService recalls; private final AppProperties props;
  private final FinishedProductRepository products; private final RecipeRepository recipes; private final ProductionBatchRepository batches;
  private final IncidentRepository incidentRepo; private final RecallCaseRepository recallRepo; private final InventoryLotRepository lots;
  SampleDataService(TenantService t, InventoryService i, ProductionService p, IncidentService in, RecallService r, AppProperties pr, FinishedProductRepository fp, RecipeRepository rp, ProductionBatchRepository bp, IncidentRepository ir, RecallCaseRepository rr, InventoryLotRepository l) { tenants=t; inventory=i; production=p; incidents=in; recalls=r; props=pr; products=fp; recipes=rp; batches=bp; incidentRepo=ir; recallRepo=rr; lots=l; }

  SampleSeedResponse seed(Membership member, SampleSeedRequest request) {
    tenants.requireAdmin(member);
    if (!props.setupCode().equals(request.setupCode())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The sample-data access code is invalid.");
    var org=member.organizationId;
    var locations=tenants.locations(member);
    var kitchen=locations.stream().filter(x->x.name().equals("Kondapur Central Kitchen")).findFirst().orElseGet(()->tenants.createLocation(member,new LocationRequest("Kondapur Central Kitchen",LocationType.CENTRAL_KITCHEN,"Kondapur, Hyderabad")));
    var banjara=locations.stream().filter(x->x.name().equals("Banjara Hills Outlet")).findFirst().orElseGet(()->tenants.createLocation(member,new LocationRequest("Banjara Hills Outlet",LocationType.OUTLET,"Banjara Hills, Hyderabad")));
    var suppliers=tenants.suppliers(member);
    var dairy=suppliers.stream().filter(x->x.name().equals("Sri Lakshmi Dairy Foods")).findFirst().orElseGet(()->tenants.createSupplier(member,new SupplierRequest("Sri Lakshmi Dairy Foods","Ravi Kumar","orders@srilakshmidairy.in","9000000001")));
    var farms=suppliers.stream().filter(x->x.name().equals("Deccan Fresh Produce")).findFirst().orElseGet(()->tenants.createSupplier(member,new SupplierRequest("Deccan Fresh Produce","Meera Shah","sales@deccanfresh.in","9000000002")));
    var ingredients=tenants.ingredients(member);
    var curd=ingredients.stream().filter(x->x.name().equals("Full-Fat Curd")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Full-Fat Curd","Dairy","kg")));
    var paneer=ingredients.stream().filter(x->x.name().equals("Paneer")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Paneer","Dairy","kg")));
    var rice=ingredients.stream().filter(x->x.name().equals("Basmati Rice")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Basmati Rice","Grain","kg")));
    link(member,dairy,curd,"SLD-CURD"); link(member,dairy,paneer,"SLD-PAN"); link(member,farms,rice,"DF-RICE");
    receive(member,"SLD-CURD-260925-A",curd.id(),dairy.id(),kitchen.id(),"20",2,18,"Sri Lakshmi Dairy Foods delivery");
    receive(member,"SLD-CURD-261002-B",curd.id(),dairy.id(),kitchen.id(),"20",1,45,"Sri Lakshmi Dairy Foods delivery");
    receive(member,"SLD-PAN-261001-C",paneer.id(),dairy.id(),kitchen.id(),"25",1,30,"Sri Lakshmi Dairy Foods delivery");
    receive(member,"DF-RICE-260930-D",rice.id(),farms.id(),banjara.id(),"50",3,90,"Deccan Fresh Produce delivery");
    var product=products.findByOrganizationIdOrderByName(org).stream().filter(x->x.name.equals("Paneer Tikka Wrap")).findFirst().orElseGet(()->{var created=production.product(member,new ProductRequest("Paneer Tikka Wrap","PTW","pieces"));return products.findById(UUID.fromString(created.id())).orElseThrow();});
    var recipe=recipes.findByOrganizationIdOrderByCreatedAtDesc(org).stream().filter(x->x.productId.equals(product.id)).findFirst().orElseGet(()->{var created=production.createRecipe(member,new RecipeRequest(product.id,"Paneer Tikka Wrap Recipe",new BigDecimal("10"),"pieces",List.of(new RecipeLineRequest(UUID.fromString(curd.id()),new BigDecimal("2"),"kg"),new RecipeLineRequest(UUID.fromString(paneer.id()),new BigDecimal("1"),"kg"))));return recipes.findById(UUID.fromString(created.id())).orElseThrow();});
    if(batches.findByOrganizationIdOrderByProducedAtDesc(org).stream().noneMatch(x->x.recipeId.equals(recipe.id))){var plan=production.preview(member,new ProductionPlanRequest(recipe.id,UUID.fromString(kitchen.id()),new BigDecimal("10"),"Lunch production run"));if(plan.ready())production.produceSuggested(member,new ProductionPlanRequest(recipe.id,UUID.fromString(kitchen.id()),new BigDecimal("10"),"Lunch production run"));}
    var recallLot=lots.findByOrganizationIdOrderByReceivedAtDesc(org).stream().filter(x->x.supplierBatchNumber.equals("SLD-CURD-260925-A")).findFirst().orElse(null);
    if(recallLot!=null&&incidentRepo.findByOrganizationIdOrderByCreatedAtDesc(org).stream().noneMatch(x->x.title.equals("Supplier cold-chain deviation — SLD-CURD-260925-A"))) incidents.create(member,new IncidentCreateRequest("Supplier cold-chain deviation — SLD-CURD-260925-A",IncidentSeverity.HIGH,List.of(recallLot.id),"Supplier delivery temperature review requested"));
    if(recallLot!=null&&recallRepo.findByOrganizationIdOrderByCreatedAtDesc(org).stream().noneMatch(x->x.title.equals("Precautionary supplier withdrawal — SLD-CURD-260925-A"))) recalls.create(member,new RecallRequest(recallLot.id,"Precautionary supplier withdrawal — SLD-CURD-260925-A"));
    expandStock(member);
    return new SampleSeedResponse("Realistic sample data loaded",tenants.locations(member).size(),tenants.suppliers(member).size(),tenants.ingredients(member).size(),inventory.inventory(member).size(),recipes.findByOrganizationIdOrderByCreatedAtDesc(org).size(),batches.findByOrganizationIdOrderByProducedAtDesc(org).size(),incidentRepo.findByOrganizationIdOrderByCreatedAtDesc(org).size(),recallRepo.findByOrganizationIdOrderByCreatedAtDesc(org).size());
  }

  private void expandStock(Membership member) {
    var locations=tenants.locations(member); var kitchen=locations.stream().filter(x->x.name().equals("Kondapur Central Kitchen")).findFirst().orElseThrow();
    var gachibowli=locations.stream().filter(x->x.name().equals("Gachibowli Outlet")).findFirst().orElseGet(()->tenants.createLocation(member,new LocationRequest("Gachibowli Outlet",LocationType.OUTLET,"Gachibowli, Hyderabad")));
    var jubilee=locations.stream().filter(x->x.name().equals("Jubilee Hills Outlet")).findFirst().orElseGet(()->tenants.createLocation(member,new LocationRequest("Jubilee Hills Outlet",LocationType.OUTLET,"Jubilee Hills, Hyderabad")));
    var suppliers=tenants.suppliers(member);
    var staples=suppliers.stream().filter(x->x.name().equals("Namma Staples & Pulses")).findFirst().orElseGet(()->tenants.createSupplier(member,new SupplierRequest("Namma Staples & Pulses","Ananya Reddy","orders@nammastaples.in","9000000003")));
    var produce=suppliers.stream().filter(x->x.name().equals("Green Basket Produce")).findFirst().orElseGet(()->tenants.createSupplier(member,new SupplierRequest("Green Basket Produce","Sana Iqbal","sales@greenbasketproduce.in","9000000004")));
    var sahyadri=suppliers.stream().filter(x->x.name().equals("Sahyadri Foods & Ingredients")).findFirst().orElseGet(()->tenants.createSupplier(member,new SupplierRequest("Sahyadri Foods & Ingredients","Vikram Joshi","supply@sahyadrifoods.in","9000000005")));
    var ingredients=tenants.ingredients(member);
    var chickpeas=ingredients.stream().filter(x->x.name().equals("Kabuli Chickpeas")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Kabuli Chickpeas","Pulses","kg")));
    var spinach=ingredients.stream().filter(x->x.name().equals("Baby Spinach")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Baby Spinach","Fresh Produce","kg")));
    var tomatoes=ingredients.stream().filter(x->x.name().equals("Roma Tomatoes")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Roma Tomatoes","Fresh Produce","kg")));
    var tofu=ingredients.stream().filter(x->x.name().equals("Firm Tofu")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Firm Tofu","Plant Protein","kg")));
    var penne=ingredients.stream().filter(x->x.name().equals("Penne Pasta")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Penne Pasta","Dry Goods","kg")));
    var mozzarella=ingredients.stream().filter(x->x.name().equals("Mozzarella")).findFirst().orElseGet(()->tenants.createIngredient(member,new IngredientRequest("Mozzarella","Dairy","kg")));
    link(member,staples,chickpeas,"NSP-CHICK");link(member,produce,spinach,"GBP-SPIN");link(member,produce,tomatoes,"GBP-TOM");link(member,sahyadri,tofu,"SFI-TOFU");link(member,sahyadri,penne,"SFI-PEN");link(member,sahyadri,mozzarella,"SFI-MOZ");
    receive(member,"NSP-CHICK-261006-A",chickpeas.id(),staples.id(),kitchen.id(),"42",6,120,"Namma Staples & Pulses delivery"); receive(member,"NSP-CHICK-261010-B",chickpeas.id(),staples.id(),gachibowli.id(),"18",2,120,"Namma Staples & Pulses delivery");
    receive(member,"GBP-SPIN-261011-A",spinach.id(),produce.id(),kitchen.id(),"9",1,5,"Green Basket Produce delivery"); receive(member,"GBP-TOM-261010-C",tomatoes.id(),produce.id(),jubilee.id(),"16",2,8,"Green Basket Produce delivery");
    receive(member,"SFI-TOFU-261008-A",tofu.id(),sahyadri.id(),kitchen.id(),"14",4,24,"Sahyadri Foods & Ingredients delivery"); receive(member,"SFI-PEN-261012-01",penne.id(),sahyadri.id(),kitchen.id(),"25",0,300,"Sahyadri Foods & Ingredients delivery"); receive(member,"SFI-PEN-261012-02",penne.id(),sahyadri.id(),gachibowli.id(),"20",0,300,"Sahyadri Foods & Ingredients delivery"); receive(member,"SFI-MOZ-261010-A",mozzarella.id(),sahyadri.id(),jubilee.id(),"12",2,20,"Sahyadri Foods & Ingredients delivery");
  }
  private void link(Membership m,SupplierResponse s,IngredientResponse i,String sku){if(!s.ingredientIds().contains(i.id()))tenants.linkIngredient(m,UUID.fromString(s.id()),new SupplierIngredientRequest(UUID.fromString(i.id()),sku));}
  private void receive(Membership m,String batch,String ingredient,String supplier,String location,String quantity,int receivedDaysAgo,int expiryDaysAhead,String note){if(inventory.inventory(m).stream().noneMatch(x->x.lot().supplierBatchNumber().equals(batch)))inventory.receive(m,new ReceiveLotRequest(UUID.fromString(ingredient),UUID.fromString(supplier),batch,LocalDate.now().minusDays(receivedDaysAgo),LocalDate.now().plusDays(expiryDaysAhead),UUID.fromString(location),new BigDecimal(quantity),"kg",note));}
}
