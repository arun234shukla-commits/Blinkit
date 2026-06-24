package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

// ============================================================================
// SPLASH SCREEN
// ============================================================================
@Composable
fun SplashScreen(viewModel: AppViewModel) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        viewModel.currentRoute.value = "onboarding"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PremiumGreenPrimary, Color(0xFF072C1B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_app_icon),
                contentDescription = "QuickBasket Logo",
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .border(2.dp, Color(0xFFD97706), RoundedCornerShape(32.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "QuickBasket",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Premium Curated Express Delivery",
                fontSize = 14.sp,
                color = Color(0xFFA3E635),
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(color = Color(0xFFFBBF24), strokeWidth = 3.dp)
        }
    }
}

// ============================================================================
// ONBOARDING SCREEN
// ============================================================================
@Composable
fun OnboardingScreen(viewModel: AppViewModel) {
    var currentPage by remember { mutableStateOf(0) }
    val pages = listOf(
        Triple(
            "Artisanal Curation",
            "Indulge in handpicked A5 Wagyu Beef, fresh organic avocados, and Kyoto Ceremonial Matcha sourced globally.",
            R.drawable.img_onboarding
        ),
        Triple(
            "Hyper-Express Delivery",
            "Cold-chain insulated courier delivery arriving right at your doorstep in under 15 minutes, preserving peak freshness.",
            R.drawable.img_hero_banner
        ),
        Triple(
            "Intelligent AI Chef",
            "Construct complete recipes instantly with our custom Gemini-powered Smart Basket builder. Try it in 1-click!",
            R.drawable.img_ai_chef
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Upper Graphic
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
                .padding(24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(PremiumGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = pages[currentPage].third),
                contentDescription = pages[currentPage].first,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Text Content Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Page indicator indicators
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pages.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .height(6.dp)
                                    .width(if (index == currentPage) 24.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == currentPage) PremiumGreenPrimary else Color.LightGray
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = pages[currentPage].first,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = pages[currentPage].second,
                        fontSize = 14.sp,
                        color = TextDarkSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { viewModel.currentRoute.value = "auth" },
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text("SKIP", color = TextDarkSecondary, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (currentPage < pages.size - 1) {
                                currentPage++
                            } else {
                                viewModel.currentRoute.value = "auth"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("onboarding_next_button")
                    ) {
                        Text(
                            text = if (currentPage == pages.size - 1) "GET STARTED" else "NEXT",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// AUTHENTICATION SCREEN (LOGIN, REGISTER, MOBILE OTP)
// ============================================================================
@Composable
fun AuthScreen(viewModel: AppViewModel) {
    val email by viewModel.authEmailInput.collectAsState()
    val password by viewModel.authPasswordInput.collectAsState()
    val name by viewModel.authNameInput.collectAsState()
    val phone by viewModel.authPhoneInput.collectAsState()
    val otpCode by viewModel.authOtpInput.collectAsState()
    val isOtpMode by viewModel.isOtpMode.collectAsState()
    val otpSent by viewModel.otpSent.collectAsState()

    var isRegisterState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative background top banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PremiumGreenPrimary, Color(0xFF1E523A))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "QuickBasket",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "VIP Gourmet Grocery Hub",
                    fontSize = 14.sp,
                    color = Color(0xFFFBBF24),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Card containing form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = when {
                            isOtpMode -> "Mobile OTP Verification"
                            isRegisterState -> "Register VIP Membership"
                            else -> "VIP Customer Login"
                        },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = AccentError,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    if (successMessage != null) {
                        Text(
                            text = successMessage!!,
                            color = AccentSuccess,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    if (isOtpMode) {
                        // OTP verification form
                        if (!otpSent) {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { viewModel.authPhoneInput.value = it },
                                label = { Text("Mobile Number") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("phone_input"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    if (viewModel.performOtpRequest(phone)) {
                                        successMessage = "Demo OTP verification code sent: 123456"
                                        errorMessage = null
                                    } else {
                                        errorMessage = "Please enter a valid mobile number"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("send_otp_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary)
                            ) {
                                Text("SEND SECURE OTP", color = Color.White)
                            }
                        } else {
                            Text(
                                text = "Verification code sent to $phone",
                                fontSize = 14.sp,
                                color = TextDarkSecondary,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = { viewModel.authOtpInput.value = it },
                                label = { Text("6-Digit OTP Code") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("otp_input"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    if (viewModel.verifyOtp(otpCode)) {
                                        viewModel.currentRoute.value = "main"
                                    } else {
                                        errorMessage = "Invalid verification code. Try 123456"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("verify_otp_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary)
                            ) {
                                Text("VERIFY & ENTER", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        TextButton(
                            onClick = {
                                viewModel.isOtpMode.value = false
                                viewModel.otpSent.value = false
                                successMessage = null
                                errorMessage = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Email Login", color = PremiumGreenPrimary)
                        }

                    } else {
                        // Email/Password Login & Register Forms
                        if (isRegisterState) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { viewModel.authNameInput.value = it },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("register_name_input")
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { viewModel.authEmailInput.value = it },
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth().testTag("email_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.authPasswordInput.value = it },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().testTag("password_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (isRegisterState) {
                                    if (name.isNotBlank() && email.contains("@") && password.length >= 4) {
                                        successMessage = "VIP account registered successfully! Logging in..."
                                        viewModel.currentRoute.value = "main"
                                    } else {
                                        errorMessage = "Please enter correct details (Password min 4 chars)"
                                    }
                                } else {
                                    if (viewModel.performLogin(email)) {
                                        viewModel.currentRoute.value = "main"
                                    } else {
                                        errorMessage = "Please enter a valid email address"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("submit_auth_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isRegisterState) "CREATE VIP MEMBERSHIP" else "SECURE SIGN IN",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.isOtpMode.value = true
                                viewModel.otpSent.value = false
                                successMessage = null
                                errorMessage = null
                            },
                            modifier = Modifier.fillMaxWidth().testTag("otp_flow_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumGreenPrimary),
                            border = BorderStroke(1.dp, PremiumGreenPrimary)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SIGN IN WITH MOBILE OTP", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Bottom Toggle switch
                if (!isOtpMode) {
                    TextButton(
                        onClick = {
                            isRegisterState = !isRegisterState
                            errorMessage = null
                            successMessage = null
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally).testTag("toggle_auth_flow")
                    ) {
                        Text(
                            text = if (isRegisterState) "Already have a membership? Sign In" else "New to QuickBasket? Join VIP Membership",
                            color = PremiumGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// MAIN CUSTOMER BOTTOM NAV LAYOUT
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCustomerScreen(viewModel: AppViewModel) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Home, 1: Categories, 2: Cart, 3: Profile
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Explore") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PremiumGreenPrimary,
                        selectedTextColor = PremiumGreenPrimary,
                        indicatorColor = PremiumGreenLight
                    ),
                    modifier = Modifier.testTag("nav_explore_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Categories") },
                    label = { Text("Categories") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PremiumGreenPrimary,
                        selectedTextColor = PremiumGreenPrimary,
                        indicatorColor = PremiumGreenLight
                    ),
                    modifier = Modifier.testTag("nav_categories_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        val cartItems by viewModel.cartItems.collectAsState()
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(containerColor = PremiumGoldSecondary) {
                                        Text(
                                            text = cartItems.sumOf { it.quantity }.toString(),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Basket") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PremiumGreenPrimary,
                        selectedTextColor = PremiumGreenPrimary,
                        indicatorColor = PremiumGreenLight
                    ),
                    modifier = Modifier.testTag("nav_basket_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Account") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PremiumGreenPrimary,
                        selectedTextColor = PremiumGreenPrimary,
                        indicatorColor = PremiumGreenLight
                    ),
                    modifier = Modifier.testTag("nav_account_tab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ExploreHomeTab(viewModel)
                1 -> CategoriesTab(viewModel)
                2 -> BasketCartTab(viewModel)
                3 -> AccountProfileTab(viewModel)
            }
        }
    }
}

// ============================================================================
// TAB 0: EXPLORE HOME TAB
// ============================================================================
@Composable
fun ExploreHomeTab(viewModel: AppViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var showAIChefDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // Upper Greeting Header
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good Evening,",
                        fontSize = 14.sp,
                        color = TextDarkSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = userProfile?.name ?: "Guest Gourmet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(PremiumGoldLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⭐ VIP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PremiumGoldSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Luxurious Glassmorphism Search Bar
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text("Search Organic Wagyu, Avocados, Matcha...", color = TextDarkSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PremiumGreenPrimary) },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PremiumGreenPrimary,
                    unfocusedBorderColor = LightBorder,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        // High-Quality Visual Hero Banner (Using generated resource!)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PremiumGreenDark, PremiumGreenPrimary)
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Summer Gourmet Sale",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.35f
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(PremiumGoldSecondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "DAILY DEAL",
                            color = Slate900Background,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Organic Fresh Farm",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = "Up to 40% OFF on all greens",
                        color = PremiumGreenLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Shop Now",
                            color = PremiumGreenDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Interactive AI Chef Banner Card (Triggers custom Gemini interface!)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAIChefDialog = true }
                    .testTag("ai_chef_banner_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Slate900Background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_ai_chef),
                        contentDescription = "AI Chef Assistant",
                        modifier = Modifier
                            .size(75.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI CHEF CO-PILOT",
                                color = Color(0xFFA3E635),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFD97706))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("HOT", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Smart Recipe Basket",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Type any recipe name to instantly build and add all organic ingredients to your cart.",
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section Title: Recommended Premium Imports
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recommended Imports",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "See All",
                    fontSize = 13.sp,
                    color = PremiumGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { /* action */ }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Horizontal list of recommended products
        item {
            val recommended = filteredProducts.filter { it.isRecommended }
            if (recommended.isEmpty()) {
                Text("No products match filters.", color = TextDarkSecondary, fontSize = 13.sp)
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(recommended) { prod ->
                        ProductCardMini(
                            product = prod,
                            onProductClick = {
                                viewModel.selectedProductDetailId.value = prod.id
                                viewModel.currentRoute.value = "product_detail"
                            },
                            onAddToCart = { viewModel.addToCart(prod.id) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section Title: Catalog Items
        item {
            Text(
                text = "Premium Pantry Catalog",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Grid of standard catalogue products
        items(filteredProducts.chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                pair.forEach { prod ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCardStandard(
                            product = prod,
                            onProductClick = {
                                viewModel.selectedProductDetailId.value = prod.id
                                viewModel.currentRoute.value = "product_detail"
                            },
                            onAddToCart = { viewModel.addToCart(prod.id) }
                        )
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }

    // AI Chef Smart Basket Dialog
    if (showAIChefDialog) {
        AIChefBasketDialog(viewModel = viewModel, onDismiss = { showAIChefDialog = false })
    }
}

// ============================================================================
// PRODUCT DISPLAY CARDS
// ============================================================================
@Composable
fun ProductCardMini(
    product: ProductEntity,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(155.dp)
            .clickable { onProductClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, LightBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Stylised background with emoji icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LightBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.imageDrawableName, fontSize = 42.sp)
                // Organic badge overlay
                if (product.isOrganic) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(PremiumGreenPrimary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("ORG", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = product.unit,
                fontSize = 11.sp,
                color = TextDarkSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PremiumGreenPrimary
                )

                IconButton(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PremiumGreenPrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProductCardStandard(
    product: ProductEntity,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, LightBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(LightBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(text = product.imageDrawableName, fontSize = 52.sp)
                if (product.isOrganic) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(PremiumGreenPrimary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("ORGANIC", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = product.unit,
                fontSize = 12.sp,
                color = TextDarkSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$${product.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PremiumGreenPrimary
                    )
                    if (product.originalPrice > product.price) {
                        Text(
                            text = "$${product.originalPrice}",
                            fontSize = 11.sp,
                            color = TextDarkSecondary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                IconButton(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PremiumGreenPrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// ============================================================================
// TAB 1: CATEGORIES TAB
// ============================================================================
@Composable
fun CategoriesTab(viewModel: AppViewModel) {
    val categories = listOf(
        "Fruits & Vegetables",
        "Dairy & Cheese",
        "Meats & Seafood",
        "Bakery & Granola",
        "Wellness & Drinks"
    )
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    val allProducts by viewModel.allProducts.collectAsState()
    val matchingProducts = allProducts.filter { it.category == selectedCategory }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left Column list of Category selection tabs
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(105.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .verticalScroll(rememberScrollState())
        ) {
            categories.forEach { cat ->
                val isSelected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = cat }
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.background else Color.Transparent
                        )
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) PremiumGreenPrimary else Color.Transparent,
                            shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp)
                        )
                        .padding(vertical = 18.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) PremiumGreenPrimary else TextDarkSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Right side displays matching product items
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = selectedCategory,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (matchingProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No premium products stocked yet in this category.", color = TextDarkSecondary, fontSize = 13.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(matchingProducts) { prod ->
                        ProductCardMini(
                            product = prod,
                            onProductClick = {
                                viewModel.selectedProductDetailId.value = prod.id
                                viewModel.currentRoute.value = "product_detail"
                            },
                            onAddToCart = { viewModel.addToCart(prod.id) }
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// TAB 2: BASKET CART TAB
// ============================================================================
@Composable
fun BasketCartTab(viewModel: AppViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val pricing by viewModel.cartPricingSummary.collectAsState()
    val activeCoupons by viewModel.activeCoupons.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()

    var showCouponSelector by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf("ASAP (10-15 Min Express Delivery)") }
    var selectedPayment by remember { mutableStateOf("UPI") }

    val slots = listOf(
        "ASAP (10-15 Min Express Delivery)",
        "Morning Rush: 8:00 AM - 10:00 AM",
        "Noon Delicacy: 12:00 PM - 2:00 PM",
        "Sunset Dinner: 6:00 PM - 8:00 PM"
    )

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(28.dp)) {
                Text("🛒", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Your Basket is Empty",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore our premium catalogs or consult the AI Chef Co-Pilot to add gourmet ingredients in 1-tap.",
                    color = TextDarkSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Gourmet Checkout Basket",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // List of cart items
            items(cartItems) { item ->
                val prod = allProducts.find { it.id == item.productId }
                if (prod != null) {
                    CartItemRow(product = prod, quantity = item.quantity, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Coupon Selector block
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, PremiumGreenLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCouponSelector = true }
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = PremiumGoldSecondary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (appliedCoupon != null) "Coupon: ${appliedCoupon!!.code}" else "Apply Promo Coupon",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = if (appliedCoupon != null) "Saving $${String.format("%.2f", pricing.discount)}" else "Tap to view active VIP discounts",
                                    fontSize = 11.sp,
                                    color = TextDarkSecondary
                                )
                            }
                        }
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            }

            // Delivery slot selection
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Delivery Slot", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                slots.forEach { slot ->
                    val isSel = slot == selectedSlot
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSlot = slot }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = isSel, onClick = { selectedSlot = slot })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = slot, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }

            // Payment methods selection
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Payment Option", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                val paymentOptions = listOf("UPI", "Credit/Debit Card", "Wallet", "Cash on Delivery (COD)")
                paymentOptions.forEach { p ->
                    val isSel = p == selectedPayment
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPayment = p }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = isSel, onClick = { selectedPayment = p })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = p, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }

            // Price calculation receipt box
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        PriceRow("Basket Subtotal", "$${String.format("%.2f", pricing.subtotal)}")
                        PriceRow("Organic Handling Tax (8%)", "$${String.format("%.2f", pricing.tax)}")
                        PriceRow("Cold-Chain Delivery", if (pricing.deliveryFee == 0.0) "FREE" else "$${pricing.deliveryFee}")
                        if (pricing.discount > 0) {
                            PriceRow("VIP Discount Applied", "-$${String.format("%.2f", pricing.discount)}", isPromo = true)
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        PriceRow("Estimated Total", "$${String.format("%.2f", pricing.total)}", isTotal = true)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Checkout CTA
            item {
                Button(
                    onClick = {
                        val orderId = viewModel.checkoutAndPlaceOrder(selectedSlot, selectedPayment)
                        if (orderId.isNotBlank()) {
                            viewModel.simulateOrderDelivery(orderId)
                            viewModel.currentRoute.value = "order_tracking"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary)
                ) {
                    Text(
                        text = "PLACE ORDER • $${String.format("%.2f", pricing.total)}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Coupon selection dialog
    if (showCouponSelector) {
        Dialog(onDismissRequest = { showCouponSelector = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Select Active VIP Coupons", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(14.dp))

                    if (activeCoupons.isEmpty()) {
                        Text("No active coupons available right now.", color = TextDarkSecondary, fontSize = 12.sp)
                    } else {
                        activeCoupons.forEach { coup ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.applyCouponCode(coup.code)
                                        showCouponSelector = false
                                    }
                                    .padding(vertical = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = PremiumGreenLight)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text(coup.code, fontWeight = FontWeight.ExtraBold, color = PremiumGreenPrimary)
                                        Text("${coup.discountPercent}% OFF", fontWeight = FontWeight.Bold, color = PremiumGoldSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(coup.description, fontSize = 11.sp, color = TextDarkSecondary)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    TextButton(onClick = { showCouponSelector = false }, modifier = Modifier.align(Alignment.End)) {
                        Text("Cancel", color = PremiumGreenPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(product: ProductEntity, quantity: Int, viewModel: AppViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PremiumGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Text(product.imageDrawableName, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(product.unit, fontSize = 11.sp, color = TextDarkSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("$${product.price} each", fontSize = 12.sp, color = PremiumGreenPrimary, fontWeight = FontWeight.Medium)
        }

        // Stepper counters
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { viewModel.updateCartItemQty(product.id, quantity - 1) },
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.Clear, contentDescription = "Decrease", modifier = Modifier.size(12.dp))
            }
            Text(text = quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            IconButton(
                onClick = { viewModel.updateCartItemQty(product.id, quantity + 1) },
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(PremiumGreenPrimary)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
fun PriceRow(label: String, valStr: String, isPromo: Boolean = false, isTotal: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 16.sp else 13.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isPromo) AccentSuccess else MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = valStr,
            fontSize = if (isTotal) 18.sp else 13.sp,
            fontWeight = if (isTotal || isPromo) FontWeight.Bold else FontWeight.Medium,
            color = if (isPromo) AccentSuccess else if (isTotal) PremiumGreenPrimary else MaterialTheme.colorScheme.onBackground
        )
    }
}

// ============================================================================
// TAB 3: ACCOUNT / PROFILE TAB
// ============================================================================
@Composable
fun AccountProfileTab(viewModel: AppViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val addresses by viewModel.addresses.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var showAddressDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // Upper VIP Badge Header
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PremiumGreenPrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = userProfile?.name ?: "Elena Rostova",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userProfile?.email ?: "elena.rostova@gourmet.com",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFD97706))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("VIP PASS", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Loyalty Point Balances", color = Color.LightGray, fontSize = 10.sp)
                            Text("${userProfile?.loyaltyPoints ?: 250} QB Points", color = Color(0xFFFBBF24), fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Referral Code", color = Color.LightGray, fontSize = 10.sp)
                            Text(userProfile?.referralCode ?: "QB_ELENA92", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Live Active Order Tracking shortcut banner
        item {
            val pendingOrder = allOrders.find { it.status == "PENDING" || it.status == "PACKING" || it.status == "ON_THE_WAY" }
            if (pendingOrder != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.activeTrackingOrderId.value = pendingOrder.orderId
                            viewModel.currentRoute.value = "order_tracking"
                        }
                        .testTag("active_order_tracking_banner"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PremiumGoldLight),
                    border = BorderStroke(1.dp, Color(0xFFD97706))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = PremiumGoldSecondary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Active Order Track Trigger", fontWeight = FontWeight.Bold, color = PremiumGoldSecondary, fontSize = 13.sp)
                            Text("Order #${pendingOrder.orderId} status: ${pendingOrder.status}", fontSize = 12.sp, color = TextDarkSecondary)
                        }
                        Text("TRACK LIVE", fontWeight = FontWeight.ExtraBold, color = PremiumGoldSecondary, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Saved Addresses list manager
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Saved VIP Addresses", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(
                    onClick = { showAddressDialog = true },
                    modifier = Modifier.testTag("add_address_button")
                ) {
                    Text("+ Add New", color = PremiumGreenPrimary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (addresses.isEmpty()) {
                Text("No addresses saved yet.", color = TextDarkSecondary, fontSize = 12.sp)
            } else {
                addresses.forEach { addr ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (addr.title == "Home") Icons.Default.Home else Icons.Default.Place,
                                    contentDescription = null,
                                    tint = PremiumGreenPrimary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(addr.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(addr.addressLine, fontSize = 11.sp, color = TextDarkSecondary)
                                }
                            }
                            if (addr.isDefault) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(PremiumGreenLight)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("DEFAULT", color = PremiumGreenPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Past Orders History list
        item {
            Text("Past Order History", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))

            if (allOrders.isEmpty()) {
                Text("Your order history will appear here.", color = TextDarkSecondary, fontSize = 12.sp)
            } else {
                allOrders.forEach { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(
                                    text = order.status,
                                    color = if (order.status == "DELIVERED") AccentSuccess else PremiumGoldSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Total Paid: $${String.format("%.2f", order.total)} | Payment: ${order.paymentMethod}", fontSize = 11.sp, color = TextDarkSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Slot: ${order.deliverySlot}", fontSize = 11.sp, color = TextDarkSecondary)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Admin hub launcher portal!
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.currentRoute.value = "admin" }
                    .testTag("admin_portal_launcher_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Slate800Surface),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFFA3E635))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Merchant Operations Portal", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("Manage live inventories, edit catalogs, and configure orders", fontSize = 11.sp, color = Color.LightGray)
                        }
                    }
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // New Address Creation Dialog
    if (showAddressDialog) {
        var aTitle by remember { mutableStateOf("Home") }
        var aName by remember { mutableStateOf("") }
        var aPhone by remember { mutableStateOf("") }
        var aLine by remember { mutableStateOf("") }
        var aCity by remember { mutableStateOf("") }
        var aZip by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showAddressDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Add Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = aTitle, onValueChange = { aTitle = it }, label = { Text("Label (e.g. Home, Office)") }, modifier = Modifier.fillMaxWidth().testTag("address_label_input"))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = aName, onValueChange = { aName = it }, label = { Text("Recipient Name") }, modifier = Modifier.fillMaxWidth().testTag("address_name_input"))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = aPhone, onValueChange = { aPhone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth().testTag("address_phone_input"))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = aLine, onValueChange = { aLine = it }, label = { Text("Street Address Line") }, modifier = Modifier.fillMaxWidth().testTag("address_line_input"))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = aCity, onValueChange = { aCity = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth().testTag("address_city_input"))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = aZip, onValueChange = { aZip = it }, label = { Text("ZIP Code") }, modifier = Modifier.fillMaxWidth().testTag("address_zip_input"))

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddressDialog = false }) {
                            Text("Cancel", color = TextDarkSecondary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (aLine.isNotBlank() && aCity.isNotBlank()) {
                                    viewModel.addAddress(aTitle, aName, aPhone, aLine, aCity, aZip)
                                    showAddressDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary),
                            modifier = Modifier.testTag("save_address_cta")
                        ) {
                            Text("Save Address", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// DIALOG: AI CHEF BASKET BUILDER (Gemini API UI Integration!)
// ============================================================================
@Composable
fun AIChefBasketDialog(viewModel: AppViewModel, onDismiss: () -> Unit) {
    val promptInput by viewModel.aiChefPrompt.collectAsState()
    val chefState by viewModel.aiChefState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🤖", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("AI Chef Co-Pilot", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Powered by Gemini 3.5 Flash", fontSize = 10.sp, color = Color.LightGray)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable workspace content
                Box(modifier = Modifier.weight(1f)) {
                    when (chefState) {
                        is AIChefState.Idle -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("👩‍🍳", fontSize = 56.sp)
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Ask the AI Chef",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Tell me what you'd like to cook! Try prompts like:\n• 'I want a high-protein salmon & burrata lunch'\n• 'Recommend matcha pancake recipe'\n• 'Ingredients for premium Kagoshima steak dinner'",
                                    color = Color.LightGray,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )
                            }
                        }

                        is AIChefState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFFA3E635))
                                Spacer(modifier = Modifier.height(14.dp))
                                Text("Chef is matching local inventory...", color = Color.LightGray, fontSize = 12.sp)
                            }
                        }

                        is AIChefState.Success -> {
                            val success = chefState as AIChefState.Success
                            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                Text(success.recipeTitle, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFA3E635))
                                Spacer(modifier = Modifier.height(6.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Slate800Surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = success.chefNote,
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Smart Recipe Ingredients:", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))

                                success.ingredients.forEach { ing ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(ing.ingredientName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                            Text(ing.quantity, color = Color.LightGray, fontSize = 10.sp)
                                        }

                                        if (ing.productId.isNotBlank()) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF0F5132))
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text("In Store", color = Color(0xFFA3E635), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        } else {
                                            Text("Optional", color = Color.Gray, fontSize = 9.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))
                                Button(
                                    onClick = {
                                        viewModel.addRecipeIngredientsToCart(success.ingredients)
                                        onDismiss()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA3E635)),
                                    modifier = Modifier.fillMaxWidth().testTag("ai_chef_add_all_cart")
                                ) {
                                    Text("ADD ALL MATCHING TO BASKET", fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }
                        }

                        is AIChefState.Error -> {
                            val err = chefState as AIChefState.Error
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("⚠️", fontSize = 38.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(err.message, color = Color.LightGray, fontSize = 12.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                // Input Box
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promptInput,
                        onValueChange = { viewModel.aiChefPrompt.value = it },
                        placeholder = { Text("What recipe should we compile?", color = Color.LightGray) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_chef_prompt_input"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA3E635),
                            unfocusedBorderColor = Color.DarkGray
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { viewModel.askAIChef(promptInput) },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFA3E635))
                            .size(45.dp)
                            .testTag("ai_chef_submit_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Submit", tint = Color.Black)
                    }
                }
            }
        }
    }
}

// ============================================================================
// SCREEN: PRODUCT DETAIL VIEW
// ============================================================================
@Composable
fun ProductDetailScreen(viewModel: AppViewModel) {
    val allProducts by viewModel.allProducts.collectAsState()
    val detailId by viewModel.selectedProductDetailId.collectAsState()
    val product = allProducts.find { it.id == detailId }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product not found.")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Upper Appbar and visual preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PremiumGreenLight, Color.White)
                        )
                    )
            ) {
                // Header buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.currentRoute.value = "main" },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PremiumGreenPrimary)
                    }

                    IconButton(
                        onClick = { viewModel.toggleWishlist(product.id) },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        val wishlist by viewModel.wishlistItems.collectAsState()
                        val inWish = wishlist.any { it.productId == product.id }
                        Icon(
                            imageVector = if (inWish) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (inWish) AccentError else PremiumGreenPrimary
                        )
                    }
                }

                // Render product Emoji icon centered
                Text(
                    text = product.imageDrawableName,
                    fontSize = 110.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Core descriptive content card
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = product.unit,
                            fontSize = 14.sp,
                            color = TextDarkSecondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${product.price}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PremiumGreenPrimary
                        )
                        if (product.originalPrice > product.price) {
                            Text(
                                text = "$${product.originalPrice}",
                                fontSize = 14.sp,
                                color = TextDarkSecondary,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Rating & Organic Badge Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PremiumGoldLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = PremiumGoldSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${product.rating} (${product.reviewCount} Reviews)", color = PremiumGoldSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (product.isOrganic) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PremiumGreenLight)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("100% ORGANIC", color = PremiumGreenPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                Divider()
                Spacer(modifier = Modifier.height(18.dp))

                Text("Product Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    color = TextDarkSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(18.dp))
                Text("Nutritional Information", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.nutritionalInfo,
                    fontSize = 13.sp,
                    color = TextDarkSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(18.dp))
                Text("Specifications", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = product.specifications,
                        fontSize = 12.sp,
                        color = TextDarkSecondary,
                        modifier = Modifier.padding(14.dp),
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = {
                        viewModel.addToCart(product.id)
                        viewModel.currentRoute.value = "main"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("detail_add_to_cart_button")
                ) {
                    Text("ADD TO BASKET • $${product.price}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

// ============================================================================
// SCREEN: ORDER TRACKING VIEW (Live Timeline Updates!)
// ============================================================================
@Composable
fun OrderTrackingScreen(viewModel: AppViewModel) {
    val trackId by viewModel.activeTrackingOrderId.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val order = allOrders.find { it.orderId == trackId }

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Order details not found.")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.currentRoute.value = "main" }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Express Live Tracking", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            // Map visual preview box (Interactive Canvas simulation!)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFE2E8F0))
            ) {
                // Background road and delivery route drawing
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(color = Color(0xFFD1D5DB)) // Slate asphalt
                }

                // Centered simulated courier details card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(14.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(PremiumGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🛵", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Rider: Carlos Mendez", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Express Delivery Courier • Insulated Box", fontSize = 11.sp, color = TextDarkSecondary)
                        }
                        Button(
                            onClick = { /* call */ },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenLight),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = PremiumGreenPrimary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Order status details timeline scroller
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Order ID: #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Placed: Today, ${order.deliverySlot}", fontSize = 12.sp, color = TextDarkSecondary)
                        }
                        Text(
                            text = order.status,
                            color = PremiumGreenPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Delivery Progress Timeline", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Timeline states PENDING -> PACKING -> ON_THE_WAY -> DELIVERED
                item {
                    val status = order.status
                    TimelineStep("Order Placed Successfully", "Your organic list is logged in the system", isCompleted = true)
                    TimelineStep("Curated Packing Hub", "Assembling fresh handpicked produce in premium cold boxes", isCompleted = status != "PENDING")
                    TimelineStep("Courier En Route", "Insulated electric transit delivering ASAP", isCompleted = status == "ON_THE_WAY" || status == "DELIVERED")
                    TimelineStep("Handed to Customer", "Arrived safely at ${order.addressTitle}", isCompleted = status == "DELIVERED", isLast = true)
                }

                // Invoice summary & close buttons
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.currentRoute.value = "main" },
                        colors = ButtonDefaults.buttonColors(containerColor = PremiumGreenPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("back_to_shop_cta")
                    ) {
                        Text("BACK TO SHOPPING", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineStep(title: String, desc: String, isCompleted: Boolean, isLast: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) PremiumGreenPrimary else Color.LightGray)
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp).align(Alignment.Center))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(if (isCompleted) PremiumGreenPrimary else Color.LightGray)
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (isCompleted) PremiumGreenPrimary else Color.Gray)
            Text(text = desc, fontSize = 11.sp, color = TextDarkSecondary)
        }
    }
}

// ============================================================================
// SCREEN: MERCHANT ADMIN DASHBOARD (Analytics, Catalogs, Stocks!)
// ============================================================================
@Composable
fun AdminDashboardScreen(viewModel: AppViewModel) {
    val products by viewModel.allProducts.collectAsState()
    val orders by viewModel.allOrders.collectAsState()

    var showAddProductSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900Background)
    ) {
        // Appbar header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.currentRoute.value = "main" }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Merchant Portal Hub", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        }

        // Live stats grid cards
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(title = "Gourmet Revenue", valStr = "$${String.format("%.2f", orders.sumOf { it.total })}", modifier = Modifier.weight(1f))
                    AdminStatCard(title = "VIP Orders Placed", valStr = "${orders.size}", modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(title = "Curation Catalog Size", valStr = "${products.size}", modifier = Modifier.weight(1f))
                    AdminStatCard(title = "Active Stock alerts", valStr = "${products.count { it.stock < 15 }} items", modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Products list management
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Products Catalog Inventory", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    Button(
                        onClick = { showAddProductSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA3E635)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("admin_add_product_cta")
                    ) {
                        Text("+ Create Product", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(products) { prod ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate800Surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(prod.imageDrawableName, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(prod.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Text("Price: $${prod.price} | Stock: ${prod.stock} left", fontSize = 11.sp, color = Color.LightGray)
                            }
                        }

                        // Stock update steppers
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = { viewModel.adminUpdateStock(prod.id, prod.stock - 5) },
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Reduce", tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                            IconButton(
                                onClick = { viewModel.adminUpdateStock(prod.id, prod.stock + 10) },
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F5132))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                            IconButton(
                                onClick = { viewModel.adminDeleteProduct(prod.id) },
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Product Sheet Dialog
    if (showAddProductSheet) {
        var pId by remember { mutableStateOf("p" + (products.size + 1)) }
        var pName by remember { mutableStateOf("") }
        var pCat by remember { mutableStateOf("Fruits & Vegetables") }
        var pPrice by remember { mutableStateOf("") }
        var pDesc by remember { mutableStateOf("") }
        var pStock by remember { mutableStateOf("50") }

        Dialog(onDismissRequest = { showAddProductSheet = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Slate800Surface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Add Product to Store catalog", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pName,
                        onValueChange = { pName = it },
                        label = { Text("Product Name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_prod_name_input"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pCat,
                        onValueChange = { pCat = it },
                        label = { Text("Category Name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_prod_cat_input"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pPrice,
                        onValueChange = { pPrice = it },
                        label = { Text("Price in Dollars", color = Color.White) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_prod_price_input"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pDesc,
                        onValueChange = { pDesc = it },
                        label = { Text("Product Description", color = Color.White) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_prod_desc_input"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pStock,
                        onValueChange = { pStock = it },
                        label = { Text("Initial Stock qty", color = Color.White) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_prod_stock_input"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddProductSheet = false }) {
                            Text("Cancel", color = Color.LightGray)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (pName.isNotBlank() && pPrice.isNotBlank()) {
                                    viewModel.adminAddProduct(
                                        id = pId,
                                        name = pName,
                                        category = pCat,
                                        price = pPrice.toDoubleOrNull() ?: 5.0,
                                        desc = pDesc,
                                        stock = pStock.toIntOrNull() ?: 50
                                    )
                                    showAddProductSheet = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA3E635)),
                            modifier = Modifier.testTag("admin_save_product_cta")
                        ) {
                            Text("Save Product", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(title: String, valStr: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Slate800Surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = Color.LightGray, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(valStr, color = Color(0xFFA3E635), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}
