package in.recallradar;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
class SearchService {
    private final SupplierRepository suppliers;
    private final IngredientRepository ingredients;
    private final LocationRepository locations;
    private final InventoryLotRepository lots;
    private final RecipeRepository recipes;
    private final RecallCaseRepository recalls;
    private final IncidentRepository incidents;

    SearchService(SupplierRepository suppliers, IngredientRepository ingredients, LocationRepository locations,
                  InventoryLotRepository lots, RecipeRepository recipes, RecallCaseRepository recalls,
                  IncidentRepository incidents) {
        this.suppliers = suppliers;
        this.ingredients = ingredients;
        this.locations = locations;
        this.lots = lots;
        this.recipes = recipes;
        this.recalls = recalls;
        this.incidents = incidents;
    }

    List<SearchResultResponse> search(Membership membership, String query) {
        String needle = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (needle.length() < 2) return List.of();

        List<SearchResultResponse> results = new ArrayList<>();
        suppliers.findByOrganizationIdOrderByName(membership.organizationId).stream().filter(x -> matches(needle, x.name))
                .forEach(x -> results.add(new SearchResultResponse("supplier", x.id.toString(), x.name, "Supplier", "ACTIVE", "/settings/suppliers/" + x.id)));
        ingredients.findByOrganizationIdOrderByName(membership.organizationId).stream().filter(x -> matches(needle, x.name))
                .forEach(x -> results.add(new SearchResultResponse("ingredient", x.id.toString(), x.name, x.category == null ? "Ingredient" : x.category, "ACTIVE", "/settings/ingredients/" + x.id)));
        locations.findByOrganizationIdOrderByName(membership.organizationId).stream().filter(x -> matches(needle, x.name))
                .forEach(x -> results.add(new SearchResultResponse("location", x.id.toString(), x.name, x.type.name().replace('_', ' '), x.active ? "ACTIVE" : "INACTIVE", "/stock/locations/" + x.id)));
        lots.findByOrganizationIdOrderByReceivedAtDesc(membership.organizationId).stream().filter(x -> matches(needle, x.supplierBatchNumber))
                .forEach(x -> results.add(new SearchResultResponse("batch", x.id.toString(), x.supplierBatchNumber, "Tracked supplier batch", "STOCK", "/command/batches/" + x.id)));
        recipes.findByOrganizationIdOrderByCreatedAtDesc(membership.organizationId).stream().filter(x -> matches(needle, x.name))
                .forEach(x -> results.add(new SearchResultResponse("recipe", x.id.toString(), x.name, "Production recipe", x.active ? "ACTIVE" : "INACTIVE", "/stock/recipes/" + x.id)));
        recalls.findByOrganizationIdOrderByCreatedAtDesc(membership.organizationId).stream().filter(x -> matches(needle, x.title))
                .forEach(x -> results.add(new SearchResultResponse("recall", x.id.toString(), x.title, "Recall case", x.status.name(), "/command/recalls/" + x.id)));
        incidents.findByOrganizationIdOrderByCreatedAtDesc(membership.organizationId).stream().filter(x -> matches(needle, x.title))
                .forEach(x -> results.add(new SearchResultResponse("incident", x.id.toString(), x.title, "Food-safety incident", x.status.name(), "/command/incidents/" + x.id)));
        return results.stream().sorted(Comparator.comparing(SearchResultResponse::type).thenComparing(SearchResultResponse::title)).limit(30).toList();
    }

    private boolean matches(String needle, String value) { return value != null && value.toLowerCase(Locale.ROOT).contains(needle); }
}
