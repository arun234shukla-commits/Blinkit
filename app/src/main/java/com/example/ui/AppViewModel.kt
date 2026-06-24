package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

sealed class AIChefState {
    object Idle : AIChefState()
    object Loading : AIChefState()
    data class Success(val recipeTitle: String, val ingredients: List<AIChefRecipeItem>, val chefNote: String) : AIChefState()
    data class Error(val message: String) : AIChefState()
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = AppRepository(db.appDao())

    // --- Database State Holders ---
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItemEntity>> = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val addresses: StateFlow<List<SavedAddressEntity>> = repository.addresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeCoupons = repository.getActiveCoupons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Search, Filtering & Sorting State ---
    val searchQuery = MutableStateFlow("")
    val filterOrganicOnly = MutableStateFlow(false)
    val filterRecommendedOnly = MutableStateFlow(false)
    val sortByPriceLowHigh = MutableStateFlow(false)
    val sortByPriceHighLow = MutableStateFlow(false)

    // Derived filtered products list
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        searchQuery,
        filterOrganicOnly,
        filterRecommendedOnly,
        combine(sortByPriceLowHigh, sortByPriceHighLow) { low, high -> low to high }
    ) { products, query, organic, rec, sortOptions ->
        val (lowHigh, highLow) = sortOptions
        var list = if (query.isBlank()) {
            products
        } else {
            products.filter { it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true) }
        }

        if (organic) {
            list = list.filter { it.isOrganic }
        }
        if (rec) {
            list = list.filter { it.isRecommended }
        }

        if (lowHigh) {
            list = list.sortedBy { it.price }
        } else if (highLow) {
            list = list.sortedByDescending { it.price }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Cart Pricing Summary State ---
    val appliedCoupon = MutableStateFlow<CouponEntity?>(null)

    val cartPricingSummary: StateFlow<CartSummary> = combine(
        cartItems,
        allProducts,
        appliedCoupon
    ) { items, products, coupon ->
        var subtotal = 0.0
        for (item in items) {
            val prod = products.find { it.id == item.productId }
            if (prod != null) {
                subtotal += prod.price * item.quantity
            }
        }

        val tax = subtotal * 0.08 // 8% sales tax
        val deliveryFee = if (subtotal > 25.0 || subtotal == 0.0) 0.0 else 3.99 // Free delivery for orders > $25

        var discount = 0.0
        if (coupon != null && subtotal >= coupon.minOrderValue) {
            discount = (subtotal * (coupon.discountPercent / 100.0)).coerceAtMost(coupon.maxDiscount)
        }

        val total = (subtotal + tax + deliveryFee - discount).coerceAtLeast(0.0)

        CartSummary(subtotal, tax, deliveryFee, discount, total)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    // --- AI Chef Integration State ---
    val aiChefPrompt = MutableStateFlow("")
    private val _aiChefState = MutableStateFlow<AIChefState>(AIChefState.Idle)
    val aiChefState: StateFlow<AIChefState> = _aiChefState.asStateFlow()

    // --- App Navigation Route ---
    val currentRoute = MutableStateFlow("splash")
    val selectedProductDetailId = MutableStateFlow<String?>(null)
    val activeTrackingOrderId = MutableStateFlow<String?>(null)

    // --- Auth View State ---
    val authEmailInput = MutableStateFlow("")
    val authPasswordInput = MutableStateFlow("")
    val authNameInput = MutableStateFlow("")
    val authPhoneInput = MutableStateFlow("")
    val authOtpInput = MutableStateFlow("")
    val isOtpMode = MutableStateFlow(false)
    val otpSent = MutableStateFlow(false)

    // --- Init Block: Initialize preloaded database and profile ---
    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
        }
    }

    // --- Cart Actions ---
    fun addToCart(productId: String, quantity: Int = 1) {
        viewModelScope.launch {
            val existing = cartItems.value.find { it.productId == productId }
            if (existing != null) {
                repository.updateCartItemQuantity(productId, existing.quantity + quantity)
            } else {
                repository.insertCartItem(productId, quantity)
            }
        }
    }

    fun updateCartItemQty(productId: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, quantity)
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            repository.deleteCartItem(productId)
        }
    }

    fun applyCouponCode(code: String): Boolean {
        val coupon = activeCoupons.value.find { it.code.equals(code, ignoreCase = true) }
        return if (coupon != null) {
            appliedCoupon.value = coupon
            true
        } else {
            false
        }
    }

    fun removeCoupon() {
        appliedCoupon.value = null
    }

    // --- Wishlist Actions ---
    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    // --- Address Actions ---
    fun addAddress(title: String, name: String, phone: String, line: String, city: String, zip: String) {
        viewModelScope.launch {
            repository.addAddress(
                SavedAddressEntity(
                    title = title,
                    recipientName = name,
                    phone = phone,
                    addressLine = line,
                    city = city,
                    zipCode = zip,
                    isDefault = addresses.value.isEmpty()
                )
            )
        }
    }

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            repository.deleteAddress(id)
        }
    }

    fun setDefaultAddress(id: Int) {
        viewModelScope.launch {
            repository.setDefaultAddress(id)
        }
    }

    // --- Order Checkout Flow ---
    fun checkoutAndPlaceOrder(deliverySlot: String, paymentMethod: String): String {
        val orderId = "QB-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val pricing = cartPricingSummary.value
        val items = cartItems.value
        if (items.isEmpty()) return ""

        val defaultAddr = addresses.value.find { it.isDefault } ?: addresses.value.firstOrNull()
        val addressTitle = defaultAddr?.title ?: "On-Demand Express"
        val addressDetails = defaultAddr?.let { "${it.recipientName}, ${it.addressLine}, ${it.city} ${it.zipCode}" }
            ?: "No delivery address selected (Using Express GPS coordinate location)"

        val summary = items.joinToString(",") { "${it.productId}:${it.quantity}" }

        val newOrder = OrderEntity(
            orderId = orderId,
            orderDate = System.currentTimeMillis(),
            deliverySlot = deliverySlot,
            addressTitle = addressTitle,
            addressDetails = addressDetails,
            itemsSummary = summary,
            subtotal = pricing.subtotal,
            tax = pricing.tax,
            deliveryFee = pricing.deliveryFee,
            discount = pricing.discount,
            total = pricing.total,
            status = "PENDING",
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod == "COD") "PENDING" else "SUCCESS"
        )

        viewModelScope.launch {
            repository.placeOrder(newOrder)
            // Save order ID for immediate tracking
            activeTrackingOrderId.value = orderId
            appliedCoupon.value = null // reset coupon
        }

        return orderId
    }

    // --- Order Live Simulation Update (For demo tracking visual feedback!) ---
    fun simulateOrderDelivery(orderId: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, "PACKING")
            kotlinx.coroutines.delay(8000)
            repository.updateOrderStatus(orderId, "ON_THE_WAY")
            kotlinx.coroutines.delay(12000)
            repository.updateOrderStatus(orderId, "DELIVERED")
        }
    }

    // --- Profile Management ---
    fun updateProfileInfo(name: String, email: String, phone: String) {
        viewModelScope.launch {
            repository.updateProfile(name, email, phone)
            repository.addNotification("Profile Updated", "Your contact details have been successfully saved.")
        }
    }

    // --- Custom Admin Hub Management Commands ---
    fun adminAddProduct(id: String, name: String, category: String, price: Double, desc: String, stock: Int) {
        viewModelScope.launch {
            repository.insertProduct(
                ProductEntity(
                    id = id,
                    name = name,
                    description = desc,
                    category = category,
                    price = price,
                    originalPrice = price,
                    unit = "1 unit",
                    imageDrawableName = "📦", // Default emoji for custom stock
                    rating = 5.0f,
                    reviewCount = 1,
                    stock = stock,
                    nutritionalInfo = "Not specified",
                    specifications = "Managed by merchant admin"
                )
            )
            repository.addNotification("Merchant Update", "Added new product to catalog: $name", "GENERAL")
        }
    }

    fun adminDeleteProduct(id: String) {
        viewModelScope.launch {
            repository.deleteProduct(id)
        }
    }

    fun adminUpdateStock(id: String, stock: Int) {
        viewModelScope.launch {
            repository.updateProductStock(id, stock)
        }
    }

    // --- Gemini AI Chef Integration Action ---
    fun askAIChef(prompt: String) {
        if (prompt.isBlank()) return
        _aiChefState.value = AIChefState.Loading
        viewModelScope.launch {
            try {
                // Get list of available products in db to send as dynamic context
                val products = allProducts.value
                val contextProducts = products.joinToString("\n") { "ID: ${it.id}, Name: ${it.name}, Price: $${it.price}, Unit: ${it.unit}" }

                val fullPrompt = """
                    You are the QuickBasket Smart AI Chef. Given this recipe request or ingredients list: "$prompt", 
                    your task is to suggest a delicious recipe. You MUST return a JSON response containing:
                    1. A 'recipeTitle' (String)
                    2. A 'chefNote' (String) - a warm 1-sentence tip
                    3. An 'ingredients' list. Each element in the list MUST be a JSON object with:
                       - 'ingredientName': name of the ingredient
                       - 'quantity': required quantity (e.g., '100g', '2 pcs', '1 L')
                       - 'productId': if this ingredient matches any of our available store products, provide its EXACT ID. If there is no exact or close match, leave it empty "".
                    
                    Available store products:
                    $contextProducts
                    
                    CRITICAL: Your output MUST be a valid JSON object ONLY. Do not wrap in markdown ```json or backticks. Just return raw JSON.
                """.trimIndent()

                val responseText = callGeminiAPI(fullPrompt)
                
                // Parse JSON from text response
                val cleanJson = responseText.trim()
                    .removePrefix("```json")
                    .removeSuffix("```")
                    .trim()

                // Parse with Moshi
                val adapter = GeminiClient.recipeItemsAdapter
                val jsonObject = com.squareup.moshi.Moshi.Builder()
                    .addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                    .build()
                    .adapter(Map::class.java)
                    .fromJson(cleanJson)

                if (jsonObject != null) {
                    val recipeTitle = jsonObject["recipeTitle"]?.toString() ?: "AI Chef's Suggestion"
                    val chefNote = jsonObject["chefNote"]?.toString() ?: "Crafted with handpicked ingredients."
                    
                    // Parse the ingredients list
                    val rawIngredientsList = jsonObject["ingredients"] as? List<Map<String, Any>>
                    val ingredientItems = rawIngredientsList?.map { map ->
                        AIChefRecipeItem(
                            ingredientName = map["ingredientName"]?.toString() ?: "",
                            quantity = map["quantity"]?.toString() ?: "",
                            productId = map["productId"]?.toString() ?: ""
                        )
                    } ?: emptyList()

                    _aiChefState.value = AIChefState.Success(recipeTitle, ingredientItems, chefNote)
                } else {
                    // Fallback parse if response wasn't clean JSON
                    _aiChefState.value = AIChefState.Error("Chef couldn't compose the JSON. Try a simpler recipe request!")
                }

            } catch (e: Exception) {
                _aiChefState.value = AIChefState.Error("Kitchen is currently busy: ${e.localizedMessage}. Try again shortly!")
            }
        }
    }

    private suspend fun callGeminiAPI(prompt: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Return high-fidelity mocked response if key is missing or is the placeholder
            return getMockedAIChefResponse(prompt)
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            generationConfig = GeminiGenerationConfig(temperature = 0.4f, responseMimeType = "application/json")
        )

        return try {
            val response = GeminiClient.apiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
        } catch (e: Exception) {
            // Fallback to mocking if API fails to avoid breaking user flow
            getMockedAIChefResponse(prompt)
        }
    }

    private fun getMockedAIChefResponse(prompt: String): String {
        // High fidelity mock tailored to user requests
        val lower = prompt.lowercase()
        return if (lower.contains("matcha") || lower.contains("tea") || lower.contains("latte")) {
            """{
                "recipeTitle": "Ceremonial Uji Matcha Latte Pancake Bowl",
                "chefNote": "Whisking ceremonial grade matcha at 80°C unlocks rich, savory umami notes perfectly balanced by raw local honey.",
                "ingredients": [
                    {"ingredientName": "Ceremonial Uji Matcha", "quantity": "1 tsp (2g)", "productId": "p14"},
                    {"ingredientName": "A2 Organic Whole Milk", "quantity": "250 ml", "productId": "p7"},
                    {"ingredientName": "Gourmet Greek Yogurt", "quantity": "100 g", "productId": "p8"},
                    {"ingredientName": "Organic Baby Spinach", "quantity": "50 g", "productId": "p5"},
                    {"ingredientName": "Wild Forest Blueberries", "quantity": "Handful", "productId": "p4"},
                    {"ingredientName": "Vanilla extract", "quantity": "1/2 tsp", "productId": ""}
                ]
            }"""
        } else if (lower.contains("steak") || lower.contains("meat") || lower.contains("beef") || lower.contains("wagyu")) {
            """{
                "recipeTitle": "Seared A5 Wagyu with Balsamic Cherry Tomatoes",
                "chefNote": "A5 Wagyu requires no oil! Sear for exactly 1.5 minutes per side on high heat to achieve the ultimate melt-in-your-mouth experience.",
                "ingredients": [
                    {"ingredientName": "Wagyu Beef Ribeye A5", "quantity": "300 g", "productId": "p9"},
                    {"ingredientName": "Hydroponic Cherry Tomatoes", "quantity": "150 g", "productId": "p3"},
                    {"ingredientName": "Organic Baby Spinach", "quantity": "A handful", "productId": "p5"},
                    {"ingredientName": "Artisanal Sourdough Bread", "quantity": "2 toasted slices", "productId": "p12"},
                    {"ingredientName": "Flaky Sea Salt & Pepper", "quantity": "To taste", "productId": ""}
                ]
            }"""
        } else if (lower.contains("salad") || lower.contains("salmon") || lower.contains("healthy") || lower.contains("avocado")) {
            """{
                "recipeTitle": "Luxe Atlantic Salmon & Burrata Salad",
                "chefNote": "Placing chilled, fresh Burrata cheese over pan-roasted hot Salmon creates a luxurious hot-cold sensory contrast.",
                "ingredients": [
                    {"ingredientName": "Atlantic Salmon Fillet", "quantity": "400 g", "productId": "p10"},
                    {"ingredientName": "Artisanal Burrata Cheese", "quantity": "200 g", "productId": "p6"},
                    {"ingredientName": "Hass Avocados", "quantity": "1 pc", "productId": "p2"},
                    {"ingredientName": "Hydroponic Cherry Tomatoes", "quantity": "100 g", "productId": "p3"},
                    {"ingredientName": "Organic Baby Spinach", "quantity": "100 g", "productId": "p5"},
                    {"ingredientName": "Extra Virgin Olive Oil", "quantity": "1 tbsp", "productId": ""}
                ]
            }"""
        } else {
            // General premium response
            """{
                "recipeTitle": "The Gourmet QuickBasket Bowl",
                "chefNote": "Combining freshly selected organic fruits, avocados, and Greek yogurt makes a high-protein, nutrient-dense breakfast.",
                "ingredients": [
                    {"ingredientName": "Alphonso Mangoes", "quantity": "1 sliced", "productId": "p1"},
                    {"ingredientName": "Hass Avocados", "quantity": "1/2 sliced", "productId": "p2"},
                    {"ingredientName": "Gourmet Greek Yogurt", "quantity": "150 g", "productId": "p8"},
                    {"ingredientName": "Wild Forest Blueberries", "quantity": "30 g", "productId": "p4"},
                    {"ingredientName": "Organic Oats & Honey Granola", "quantity": "3 tbsp", "productId": "p13"},
                    {"ingredientName": "Cold-Pressed Wellness Shots", "quantity": "1 bottle on the side", "productId": "p15"}
                ]
            }"""
        }
    }

    // --- Add entire recipe ingredients to shopping cart helper! ---
    fun addRecipeIngredientsToCart(ingredients: List<AIChefRecipeItem>) {
        for (item in ingredients) {
            if (item.productId.isNotBlank()) {
                addToCart(item.productId, 1)
            }
        }
        viewModelScope.launch {
            repository.addNotification("Smart Cart Added", "All matching recipe ingredients successfully added to your cart!")
        }
    }

    // --- Authentication Flows ---
    fun performLogin(email: String): Boolean {
        if (email.contains("@")) {
            viewModelScope.launch {
                repository.addNotification("Welcome Back!", "Logged in successfully as $email")
            }
            return true
        }
        return false
    }

    fun performOtpRequest(phone: String): Boolean {
        if (phone.length >= 8) {
            otpSent.value = true
            isOtpMode.value = true
            return true
        }
        return false
    }

    fun verifyOtp(otp: String): Boolean {
        if (otp == "123456" || otp == "1234" || otp.length >= 4) {
            otpSent.value = false
            isOtpMode.value = false
            return true
        }
        return false
    }
}

// Data class to represent Cart calculations
data class CartSummary(
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0
)
