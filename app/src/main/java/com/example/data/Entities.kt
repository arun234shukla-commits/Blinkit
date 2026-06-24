package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    val originalPrice: Double, // For discount styling
    val unit: String,
    val imageDrawableName: String, // e.g. "img_hero_banner" or custom vectors
    val rating: Float,
    val reviewCount: Int,
    val stock: Int,
    val nutritionalInfo: String = "Per 100g: Calories 50kcal, Organic",
    val specifications: String = "Premium quality curation selection",
    val isOrganic: Boolean = true,
    val isRecommended: Boolean = false
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val quantity: Int
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey val productId: String
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val orderDate: Long,
    val deliverySlot: String,
    val addressTitle: String,
    val addressDetails: String,
    val itemsSummary: String, // format "productId:qty,productId:qty"
    val subtotal: Double,
    val tax: Double,
    val deliveryFee: Double,
    val discount: Double,
    val total: Double,
    val status: String, // PENDING, PACKING, ON_THE_WAY, DELIVERED, CANCELLED
    val paymentMethod: String, // UPI, CARD, WALLET, COD
    val paymentStatus: String // SUCCESS, PENDING
)

@Entity(tableName = "saved_addresses")
data class SavedAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, // Home, Office, Gym, etc.
    val recipientName: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val zipCode: String,
    val isDefault: Boolean = false
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val description: String,
    val discountPercent: Double,
    val minOrderValue: Double,
    val maxDiscount: Double,
    val isActive: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: String // PROMO, ORDER_STATUS, GENERAL
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "user_default",
    val name: String,
    val email: String,
    val phone: String,
    val referralCode: String,
    val referredBy: String? = null,
    val loyaltyPoints: Int = 120,
    val isPremium: Boolean = true
)
