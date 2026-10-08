package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HogwashData
import com.example.model.ClassScheduleItem
import com.example.model.SubjectItem
import com.example.ui.components.RubberSegment
import com.example.ui.theme.*
import com.example.viewmodel.HogwashViewModel

@Composable
fun TimetableScreen(
    viewModel: HogwashViewModel,
    onGuideMeThere: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDay by viewModel.selectedDay.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Schedule, 1 = Subjects
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    val nextClass = HogwashData.TIMETABLE.firstOrNull { it.dayOfWeek == selectedDay }
        ?: HogwashData.TIMETABLE.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Tab Selector (Timetable vs Subjects) in Frosted Glass
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = GlassSurface,
                border = BorderStroke(1.dp, GlassBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GlassTabButton(
                        title = "📅 Class Timetable",
                        isSelected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    GlassTabButton(
                        title = "📜 Subjects & Classes",
                        isSelected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (activeTab == 0) {
            // "What class do I have next?" Hero Glass Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, glassBorderBrush(0.5f, 0.15f), RoundedCornerShape(20.dp)),
                    color = GlassCardElevated
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⏰ UPCOMING LECTURE",
                                color = GlassGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(
                                color = Color(0x33FFFFFF),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, GlassBorderSubtle)
                            ) {
                                Text(
                                    text = nextClass.timeSlot,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = nextClass.subjectName,
                            color = TextPrimary,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "Instructor: ${nextClass.professor} • Room: ${nextClass.locationName}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = Color(0x33000000),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GlassBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "💡", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = nextClass.comedicNote,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.setDestination(nextClass.locationId)
                                    onGuideMeThere(nextClass.locationId)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassGold),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("guide_me_there_button")
                            ) {
                                Text(
                                    text = "Guide Me There ➔",
                                    color = Color(0xFF0B0910),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = { viewModel.attendClass(nextClass) },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassCard),
                                border = BorderStroke(1.dp, GlassBorder),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("attend_class_button")
                            ) {
                                Text(
                                    text = "Attend (+35 XP)",
                                    color = GlassGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Day Filter RubberSegment
            item {
                RubberSegment(
                    items = days,
                    defaultValue = selectedDay,
                    onChange = { value: String, index: Int -> viewModel.selectDay(value) },
                    trackColor = Color(0xFF230308),
                    thumbColor = GlassGold,
                    textColor = TextSecondary,
                    activeTextColor = Color(0xFF0B0910),
                    radius = 10.dp,
                    inset = 3.dp,
                    draggable = true
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Schedule Items for Selected Day
            val dayClasses = HogwashData.TIMETABLE.filter { it.dayOfWeek == selectedDay }
            if (dayClasses.isEmpty()) {
                item {
                    Text(
                        text = "No scheduled lectures on $selectedDay! Free time to avoid the poltergeist.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            } else {
                items(dayClasses) { item ->
                    GlassScheduleCard(
                        item = item,
                        onGuideClick = {
                            viewModel.setDestination(item.locationId)
                            onGuideMeThere(item.locationId)
                        },
                        onAttendClick = { viewModel.attendClass(item) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        } else {
            // Subjects Catalog
            items(HogwashData.SUBJECTS) { subject ->
                GlassSubjectCard(
                    subject = subject,
                    isRecommended = profile.house == subject.recommendedForHouse,
                    onNavigateToClass = {
                        viewModel.setDestination(subject.locationId)
                        onGuideMeThere(subject.locationId)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun GlassScheduleCard(
    item: ClassScheduleItem,
    onGuideClick: () -> Unit,
    onAttendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = GlassCard,
        border = BorderStroke(1.dp, GlassBorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.timeSlot,
                    color = GlassGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = item.locationName,
                    color = TextDim,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.subjectName,
                color = TextPrimary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = "Taught by ${item.professor} • Req: ${item.requiredSupply}",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onGuideClick,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GlassBorder),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "Navigate 👣", color = GlassGold, fontSize = 11.sp)
                }

                Button(
                    onClick = onAttendClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassCardElevated),
                    border = BorderStroke(1.dp, GlassBorderSubtle),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "Attend Lecture", color = TextPrimary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun GlassSubjectCard(
    subject: SubjectItem,
    isRecommended: Boolean,
    onNavigateToClass: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = GlassCard,
        border = BorderStroke(1.dp, if (isRecommended) GlassGold.copy(alpha = 0.6f) else GlassBorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = subject.name,
                    color = GlassGold,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isRecommended) {
                    Surface(
                        color = Color(0x33DC2626),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color(0x66DC2626))
                    ) {
                        Text(
                            text = "★ For Your House",
                            color = Color(0xFFFCA5A5),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "Head Professor: ${subject.professor}",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subject.description,
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "⚠️ Warning: ${subject.humorousWarning}",
                color = GlassAmber,
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onNavigateToClass,
                colors = ButtonDefaults.buttonColors(containerColor = GlassCardElevated),
                border = BorderStroke(1.dp, GlassBorder),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Find Classroom on Map ➔",
                    color = GlassGold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
