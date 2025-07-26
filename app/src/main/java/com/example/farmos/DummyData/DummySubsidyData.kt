package com.example.farmos.data

import com.example.farmos.model.Subsidy

val dummySubsidies = listOf(
    Subsidy(
        schemeName = "PM Kisan Yojana",
        state = "Karnataka",
        crop = "Paddy",
        eligibility = "All small farmers",
        benefit = "₹6000 yearly support",
        applicationLink = "https://pmkisan.gov.in"
    ),
    Subsidy(
        schemeName = "Organic Farming Scheme",
        state = "Maharashtra",
        crop = "Sugarcane",
        eligibility = "Organic certification needed",
        benefit = "₹10000 per acre support",
        applicationLink = "https://mahaorganic.gov.in"
    ),
    Subsidy(
        schemeName = "Solar Pump Subsidy",
        state = "Tamil Nadu",
        crop = "General",
        eligibility = "Own farmland",
        benefit = "50% subsidy on solar pumps",
        applicationLink = "https://tnsolar.gov.in"
    )
)
