package com.example.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    val allProducts: Flow<List<ProductEntity>> = appDao.getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = appDao.getCartItems()
    val wishlistItems: Flow<List<WishlistItemEntity>> = appDao.getWishlistItems()
    val allOrders: Flow<List<OrderEntity>> = appDao.getAllOrders()
    val addresses: Flow<List<SavedAddressEntity>> = appDao.getAddresses()
    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()
    val notifications: Flow<List<NotificationEntity>> = appDao.getNotifications()

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return appDao.getProductsByCategory(category)
    }

    fun getProductById(id: String): Flow<ProductEntity?> {
        return appDao.getProductById(id)
    }

    fun isProductInWishlist(productId: String): Flow<Boolean> {
        return appDao.isProductInWishlist(productId)
    }

    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return appDao.searchProducts(query)
    }

    fun getOrderById(orderId: String): Flow<OrderEntity?> {
        return appDao.getOrderById(orderId)
    }

    fun getActiveCoupons(): Flow<List<CouponEntity>> {
        return appDao.getActiveCoupons()
    }

    suspend fun insertProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        appDao.insertProduct(product)
    }

    suspend fun deleteProduct(id: String) = withContext(Dispatchers.IO) {
        appDao.deleteProduct(id)
    }

    suspend fun updateProductStock(id: String, stock: Int) = withContext(Dispatchers.IO) {
        appDao.updateProductStock(id, stock)
    }

    suspend fun insertCartItem(productId: String, quantity: Int) = withContext(Dispatchers.IO) {
        appDao.insertCartItem(CartItemEntity(productId, quantity))
    }

    suspend fun updateCartItemQuantity(productId: String, quantity: Int) = withContext(Dispatchers.IO) {
        if (quantity <= 0) {
            appDao.deleteCartItem(productId)
        } else {
            appDao.updateCartItemQuantity(productId, quantity)
        }
    }

    suspend fun deleteCartItem(productId: String) = withContext(Dispatchers.IO) {
        appDao.deleteCartItem(productId)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        appDao.clearCart()
    }

    suspend fun toggleWishlist(productId: String) = withContext(Dispatchers.IO) {
        val inWishlist = appDao.isProductInWishlist(productId).first()
        if (inWishlist) {
            appDao.deleteWishlistItem(productId)
        } else {
            appDao.insertWishlistItem(WishlistItemEntity(productId))
        }
    }

    suspend fun addAddress(address: SavedAddressEntity) = withContext(Dispatchers.IO) {
        appDao.insertAddress(address)
    }

    suspend fun deleteAddress(id: Int) = withContext(Dispatchers.IO) {
        appDao.deleteAddress(id)
    }

    suspend fun setDefaultAddress(id: Int) = withContext(Dispatchers.IO) {
        appDao.setDefaultAddress(id)
    }

    suspend fun placeOrder(order: OrderEntity) = withContext(Dispatchers.IO) {
        appDao.insertOrder(order)
        // Deduct stock for each item in the order
        val items = parseItemsSummary(order.itemsSummary)
        for ((pId, qty) in items) {
            val product = appDao.getProductById(pId).first()
            if (product != null) {
                val newStock = (product.stock - qty).coerceAtLeast(0)
                appDao.updateProductStock(pId, newStock)
            }
        }
        // Update user loyalty points
        val pointsEarned = (order.total / 10).toInt()
        appDao.addLoyaltyPoints(pointsEarned)

        // Clear cart
        appDao.clearCart()

        // Insert notification
        appDao.insertNotification(
            NotificationEntity(
                title = "Order Placed Successfully",
                message = "Your order #${order.orderId} of $${String.format("%.2f", order.total)} is confirmed!",
                timestamp = System.currentTimeMillis(),
                type = "ORDER_STATUS"
            )
        )
    }

    suspend fun updateOrderStatus(orderId: String, status: String) = withContext(Dispatchers.IO) {
        appDao.updateOrderStatus(orderId, status)
        appDao.insertNotification(
            NotificationEntity(
                title = "Order Status Update",
                message = "Your order #${orderId} status updated to: $status",
                timestamp = System.currentTimeMillis(),
                type = "ORDER_STATUS"
            )
        )
    }

    suspend fun updateProfile(name: String, email: String, phone: String) = withContext(Dispatchers.IO) {
        appDao.updateUserProfile(name, email, phone)
    }

    suspend fun addNotification(title: String, message: String, type: String = "GENERAL") = withContext(Dispatchers.IO) {
        appDao.insertNotification(
            NotificationEntity(
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = type
            )
        )
    }

    suspend fun insertCoupon(coupon: CouponEntity) = withContext(Dispatchers.IO) {
        appDao.insertCoupon(coupon)
    }

    suspend fun deleteCoupon(code: String) = withContext(Dispatchers.IO) {
        appDao.deleteCoupon(code)
    }

    // --- Helper to parse formatting "id:qty,id:qty" ---
    fun parseItemsSummary(summary: String): List<Pair<String, Int>> {
        if (summary.isBlank()) return emptyList()
        return try {
            summary.split(",").map {
                val parts = it.split(":")
                parts[0] to parts[1].toInt()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val existingProducts = appDao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            populateProducts()
            populateDefaultUser()
            populateDefaultAddresses()
            populateDefaultCoupons()
            populateDefaultNotifications()
        }
    }

    private suspend fun populateProducts() {
        val products = listOf(
            // Produce
            ProductEntity(
                id = "p1", name = "Alphonso Mangoes", category = "Fruits & Vegetables",
                price = 12.99, originalPrice = 15.99, unit = "1 kg", imageDrawableName = "🥑",
                rating = 4.9f, reviewCount = 142, stock = 45, isOrganic = true, isRecommended = true,
                description = "Handpicked organic Alphonso mangoes, known as the king of mangoes. Rich in flavor, fiber, and vitamin C.",
                specifications = "Source: Devgad, India\nType: Fresh Alphonso\nGrade: Premium"
            ),
            ProductEntity(
                id = "p2", name = "Hass Avocados", category = "Fruits & Vegetables",
                price = 5.49, originalPrice = 6.99, unit = "3 pcs", imageDrawableName = "🥑",
                rating = 4.8f, reviewCount = 89, stock = 60, isOrganic = true, isRecommended = true,
                description = "Perfectly ripe organic Hass avocados. Ideal for breakfast toast, salads, or authentic fresh guacamole.",
                specifications = "Origin: Mexico\nPackaging: Mesh bag\nRipeness: Ready to eat"
            ),
            ProductEntity(
                id = "p3", name = "Hydroponic Cherry Tomatoes", category = "Fruits & Vegetables",
                price = 3.99, originalPrice = 4.49, unit = "250 g", imageDrawableName = "🍅",
                rating = 4.6f, reviewCount = 54, stock = 30, isOrganic = true, isRecommended = false,
                description = "Super sweet, vine-ripened hydroponic cherry tomatoes grown without pesticides. Crisp and burst-in-the-mouth texture.",
                specifications = "Type: Red Cherry\nFarming: Hydroponic\nState: Vine-on"
            ),
            ProductEntity(
                id = "p4", name = "Wild Forest Blueberries", category = "Fruits & Vegetables",
                price = 6.99, originalPrice = 8.49, unit = "125 g", imageDrawableName = "🫐",
                rating = 4.9f, reviewCount = 210, stock = 25, isOrganic = true, isRecommended = true,
                description = "Hand-gathered wild forest blueberries, bursting with natural antioxidants. Excellent for yogurt toppers or pancakes.",
                specifications = "Source: Clean wild forests\nState: Chilled\nSize: Extra large"
            ),
            ProductEntity(
                id = "p5", name = "Organic Baby Spinach", category = "Fruits & Vegetables",
                price = 2.99, originalPrice = 3.49, unit = "150 g", imageDrawableName = "🥬",
                rating = 4.7f, reviewCount = 76, stock = 50, isOrganic = true, isRecommended = false,
                description = "Tender baby spinach leaves pre-washed and ready to cook or toss. Cultivated naturally on family-owned organic farms.",
                specifications = "Variety: Baby Spinach\nState: Pre-washed\nPackaging: Eco-clamshell"
            ),

            // Dairy
            ProductEntity(
                id = "p6", name = "Artisanal Burrata Cheese", category = "Dairy & Cheese",
                price = 7.99, originalPrice = 9.99, unit = "200 g", imageDrawableName = "🧀",
                rating = 4.9f, reviewCount = 115, stock = 20, isOrganic = false, isRecommended = true,
                description = "Ultra-creamy artisanal burrata. Standard-setting outer mozzarella shell filled with rich, luscious stracciatella cream.",
                specifications = "Source: Local Italian cheesemaker\nType: Fresh burrata\nShelf life: 5 days"
            ),
            ProductEntity(
                id = "p7", name = "A2 Organic Whole Milk", category = "Dairy & Cheese",
                price = 3.49, originalPrice = 3.49, unit = "1 L", imageDrawableName = "🥛",
                rating = 4.7f, reviewCount = 310, stock = 100, isOrganic = true, isRecommended = false,
                description = "Pure, fresh A2 organic whole milk from happy grass-fed cows. Gentle on digestion, rich in calcium and omega-3s.",
                specifications = "Source: Local organic dairy\nType: Pasteurized, non-homogenized\nFat content: 3.5%"
            ),
            ProductEntity(
                id = "p8", name = "Gourmet Greek Yogurt", category = "Dairy & Cheese",
                price = 4.49, originalPrice = 4.99, unit = "500 g", imageDrawableName = "🍨",
                rating = 4.8f, reviewCount = 180, stock = 40, isOrganic = true, isRecommended = true,
                description = "Thick, strained organic Greek yogurt prepared with live active cultures. Packed with protein and a velvety smooth taste.",
                specifications = "Type: Plain Strained\nProtein: 15g per serving\nOrganic: Yes"
            ),

            // Meats & Seafood
            ProductEntity(
                id = "p9", name = "Wagyu Beef Ribeye A5", category = "Meats & Seafood",
                price = 49.99, originalPrice = 59.99, unit = "300 g", imageDrawableName = "🥩",
                rating = 5.0f, reviewCount = 67, stock = 12, isOrganic = false, isRecommended = true,
                description = "Authentic Japanese A5 Wagyu beef ribeye. Showcases the legendary rich marbling, melt-in-your-mouth fat structure, and savory umami profile.",
                specifications = "Grade: A5 Wagyu\nCut: Ribeye Steak\nOrigin: Kagoshima, Japan"
            ),
            ProductEntity(
                id = "p10", name = "Atlantic Salmon Fillet", category = "Meats & Seafood",
                price = 18.99, originalPrice = 22.49, unit = "400 g", imageDrawableName = "🐟",
                rating = 4.8f, reviewCount = 128, stock = 18, isOrganic = false, isRecommended = true,
                description = "Premium fresh Atlantic salmon fillet. Rich in essential omega-3 fatty acids, tender, and perfect for pan-searing or grilling.",
                specifications = "Type: Skin-on, boneless\nState: Chilled\nSource: Sustainable farms"
            ),
            ProductEntity(
                id = "p11", name = "Free-Range Chicken Breast", category = "Meats & Seafood",
                price = 11.49, originalPrice = 12.99, unit = "500 g", imageDrawableName = "🍗",
                rating = 4.5f, reviewCount = 92, stock = 40, isOrganic = true, isRecommended = false,
                description = "100% organic, air-chilled chicken breast fillets. Raised free-range on a non-GMO organic vegetable feed program.",
                specifications = "Variety: Chicken Breast\nAntibiotics: None\nOrganic: Yes"
            ),

            // Bakery
            ProductEntity(
                id = "p12", name = "Artisanal Sourdough Bread", category = "Bakery & Granola",
                price = 4.99, originalPrice = 5.99, unit = "1 unit", imageDrawableName = "🍞",
                rating = 4.9f, reviewCount = 156, stock = 15, isOrganic = true, isRecommended = true,
                description = "Sourdough bread slow-fermented for 36 hours using an heirloom starter. Golden-brown blistered crust with a soft, sour interior.",
                specifications = "Ingredients: Organic flour, spring water, sea salt\nWeight: 650g\nType: Wild yeast starter"
            ),
            ProductEntity(
                id = "p13", name = "Gluten-Free Almond Croissant", category = "Bakery & Granola",
                price = 5.99, originalPrice = 6.99, unit = "2 pcs", imageDrawableName = "🥐",
                rating = 4.7f, reviewCount = 43, stock = 22, isOrganic = false, isRecommended = false,
                description = "Decadent gluten-free croissants laminated with grass-fed butter, filled with organic almond frangipane, and topped with toasted almond flakes.",
                specifications = "Gluten-Free: Yes\nSweetener: Organic cane sugar\nServing suggestion: Warm before eating"
            ),

            // Wellness & Matcha
            ProductEntity(
                id = "p14", name = "Ceremonial Uji Matcha", category = "Wellness & Drinks",
                price = 24.99, originalPrice = 29.99, unit = "30 g", imageDrawableName = "🍵",
                rating = 4.9f, reviewCount = 82, stock = 15, isOrganic = true, isRecommended = true,
                description = "First-harvest ceremonial grade matcha stone-ground in Uji, Japan. Vibrant emerald green color with a natural sweet and savory creaminess.",
                specifications = "Grade: Ceremonial\nOrigin: Uji, Kyoto, Japan\nPackaging: Vacuum-sealed tin"
            ),
            ProductEntity(
                id = "p15", name = "Cold-Pressed Wellness Shots", category = "Wellness & Drinks",
                price = 8.99, originalPrice = 10.99, unit = "4 pcs", imageDrawableName = "🧪",
                rating = 4.8f, reviewCount = 95, stock = 35, isOrganic = true, isRecommended = true,
                description = "Four concentrated cold-pressed wellness shots packed with ginger, turmeric, organic lemon, cayenne, and honey to boost immunity.",
                specifications = "Quantity: 4 bottles x 60 ml\nProcessing: High-pressure processed (HPP)\nSugar added: None"
            ),
            ProductEntity(
                id = "p16", name = "Ginger-Lemon Kombucha", category = "Wellness & Drinks",
                price = 3.29, originalPrice = 3.79, unit = "330 ml", imageDrawableName = "🍾",
                rating = 4.6f, reviewCount = 61, stock = 48, isOrganic = true, isRecommended = false,
                description = "Effervescent fermented black tea infused with cold-pressed ginger and Meyer lemon juice. Probiotic-rich and refreshing.",
                specifications = "Variety: Ginger Lemon\nType: Raw unpasteurized\nOrganic: Yes"
            )
        )
        appDao.insertProducts(products)
    }

    private suspend fun populateDefaultUser() {
        appDao.insertUserProfile(
            UserProfileEntity(
                id = "user_default",
                name = "Elena Rostova",
                email = "elena.rostova@gourmet.com",
                phone = "+1 (555) 732-9210",
                referralCode = "QB_ELENA92",
                referredBy = null,
                loyaltyPoints = 250,
                isPremium = true
            )
        )
    }

    private suspend fun populateDefaultAddresses() {
        val addresses = listOf(
            SavedAddressEntity(
                id = 1,
                title = "Home",
                recipientName = "Elena Rostova",
                phone = "+1 (555) 732-9210",
                addressLine = "742 Evergreen Terrace, Penthouse B",
                city = "Springfield",
                zipCode = "90210",
                isDefault = true
            ),
            SavedAddressEntity(
                id = 2,
                title = "Office",
                recipientName = "Elena Rostova (TechCorp)",
                phone = "+1 (555) 732-9211",
                addressLine = "100 Infinite Loop, Building 3, Fl 4",
                city = "Cupertino",
                zipCode = "95014",
                isDefault = false
            )
        )
        for (addr in addresses) {
            appDao.insertAddress(addr)
        }
    }

    private suspend fun populateDefaultCoupons() {
        val coupons = listOf(
            CouponEntity("WELCOME50", "Get 50% off on your very first organic order up to $20.", 50.0, 15.0, 20.0),
            CouponEntity("ORGANICVIP", "Get 15% off on our handpicked Organic Produce collections.", 15.0, 30.0, 50.0),
            CouponEntity("MATCHALOVE", "Save $5.00 on luxury ceremonial grade matcha and tea products.", 20.0, 25.0, 5.0)
        )
        for (c in coupons) {
            appDao.insertCoupon(c)
        }
    }

    private suspend fun populateDefaultNotifications() {
        val notifications = listOf(
            NotificationEntity(
                title = "Welcome to QuickBasket!",
                message = "Experience premium grocery delivery with 36-hour slow fermented bread, Wagyu A5, and Ceremonial Matcha delivered to your doorstep in minutes.",
                timestamp = System.currentTimeMillis() - 86400000,
                type = "GENERAL"
            ),
            NotificationEntity(
                title = "A5 Wagyu Restocked!",
                message = "The highly requested Kagoshima A5 Wagyu Beef is back in limited stock. Reserve yours before the dinner rush!",
                timestamp = System.currentTimeMillis() - 10800000,
                type = "PROMO"
            )
        )
        for (n in notifications) {
            appDao.insertNotification(n)
        }
    }
}
