package campusfind.demo.service;

import campusfind.demo.dto.ItemMatch;
import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import campusfind.demo.entity.LostReport;
import campusfind.demo.entity.LostStatus;
import campusfind.demo.repository.FoundItemRepository;
import campusfind.demo.repository.LostReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class MatchService {

    private final LostReportRepository lostReportRepository;
    private final FoundItemRepository foundItemRepository;

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "a", "an", "and", "or", "in", "on", "at", "to", "for", "of", "with", "by", "from",
            "is", "was", "are", "were", "my", "i", "it", "near", "around", "block", "floor"
    ));

    public MatchService(LostReportRepository lostReportRepository, FoundItemRepository foundItemRepository) {
        this.lostReportRepository = lostReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public List<ItemMatch> findMatches() {
        List<LostReport> openLostReports = lostReportRepository.findByStatus(LostStatus.OPEN);
        List<FoundItem> activeFoundItems = foundItemRepository.findByStatus(FoundStatus.AVAILABLE);
        activeFoundItems.addAll(foundItemRepository.findByStatus(FoundStatus.CLAIMED));

        List<ItemMatch> matches = new ArrayList<>();

        for (LostReport lost : openLostReports) {
            for (FoundItem found : activeFoundItems) {
                // Must be in the same category
                if (lost.getCategory().getId().equals(found.getCategory().getId())) {
                    int score = 50; // Base score for same category
                    List<String> reasons = new ArrayList<>();
                    reasons.add("Category matches: '" + lost.getCategory().getName() + "'");

                    // Keyword matching in title and description
                    Set<String> lostWords = extractKeywords(lost.getTitle() + " " + lost.getDescription());
                    Set<String> foundWords = extractKeywords(found.getTitle() + " " + found.getDescription());

                    Set<String> commonKeywords = new HashSet<>(lostWords);
                    commonKeywords.retainAll(foundWords);

                    if (!commonKeywords.isEmpty()) {
                        score += Math.min(35, commonKeywords.size() * 15);
                        reasons.add("Matching keywords: " + String.join(", ", commonKeywords));
                    }

                    // Location matching
                    if (isLocationSimilar(lost.getLocation(), found.getLocation())) {
                        score += 15;
                        reasons.add("Similar location: '" + lost.getLocation() + "' & '" + found.getLocation() + "'");
                    }

                    matches.add(new ItemMatch(lost, found, String.join(" | ", reasons), Math.min(100, score)));
                }
            }
        }

        // Sort by match score descending
        matches.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));
        return matches;
    }

    private Set<String> extractKeywords(String text) {
        Set<String> words = new HashSet<>();
        if (text == null) return words;

        String[] tokens = text.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+");
        for (String token : tokens) {
            if (token.length() > 2 && !STOP_WORDS.contains(token)) {
                words.add(token);
            }
        }
        return words;
    }

    private boolean isLocationSimilar(String loc1, String loc2) {
        if (loc1 == null || loc2 == null) return false;
        String l1 = loc1.trim().toLowerCase();
        String l2 = loc2.trim().toLowerCase();
        return l1.contains(l2) || l2.contains(l1);
    }
}
