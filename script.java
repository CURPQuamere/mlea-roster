<script>
const csvUrl = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSAbS7bw1SORtP8GVyQ95rb7h_JcRJ0sQR6xPB5-aX9vXxgxTUlXCv1oenjnvoBp72Exm69dcoqKaq9/pub?output=csv';

async function updateRoster() {
    try {
        const response = await fetch(csvUrl);
        const text = await response.text();
        // Clean up weird line breaks from Google Sheets
        const rows = text.replace(/\r/g, "").split('\n'); 

        const d1Body = document.getElementById('dist-1-body');
        const d2Body = document.getElementById('dist-2-body');
        
        if(d1Body) d1Body.innerHTML = '';
        if(d2Body) d2Body.innerHTML = '';

        rows.forEach((row, index) => {
            // Skip the header rows
            if (index < 2 || !row.trim()) return;

            // Split by comma but respect quotes
            const cols = row.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/); 

            // Correct Mapping for your specific CSV structure:
            // Column E (Index 4) = Badge
            // Column G (Index 6) = Rank (Usually)
            // Column H (Index 7) = Name
            // Column I (Index 8) = Status
            const badge  = cols[4] ? cols[4].trim() : "";
            const rank   = cols[6] ? cols[6].replace(/"/g, "").trim() : "Officer";
            const name   = cols[7] ? cols[7].replace(/"/g, "").trim() : "";
            const status = cols[8] ? cols[8].trim() : "";
            const joined = cols[9] ? cols[9].trim() : "N/A";

            // If name is blank at index 7, try index 6 (sometimes columns shift)
            const finalName = name || (cols[6] && !cols[6].includes("Dist") ? cols[6].replace(/"/g, "").trim() : "Unknown");

            // Display everyone regardless of status (per your previous request)
            if (finalName && badge) {
                
                // Logic: Badges 1xxx/2xxx = Dist 1, others = Dist 2
                let district = (badge.startsWith('1') || badge.startsWith('2')) ? '1' : '2';
                const targetBody = document.getElementById(`dist-${district}-body`);
                
                if (targetBody) {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td class="col-id">${badge}</td>
                        <td><span class="rank-cell">${rank}</span> ${finalName}</td>
                        <td class="date-col">${joined}</td>
                        <td class="date-col">--</td>
                        <td><span class="tag fto">${status.toUpperCase() || 'ACTIVE'}</span></td>
                    `;
                    targetBody.appendChild(tr);
                }
            }
        });
    } catch (err) {
        console.error("Roster sync error:", err);
    }
}

updateRoster();
</script>
