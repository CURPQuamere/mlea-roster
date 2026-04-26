const csvUrl = 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSAbS7bw1SORtP8GVyQ95rb7h_JcRJ0sQR6xPB5-aX9vXxgxTUlXCv1oenjnvoBp72Exm69dcoqKaq9/pub?output=csv';

async function updateRoster() {
    try {
        const response = await fetch(csvUrl);
        const text = await response.text();
        
        // Split by lines
        const rows = text.split('\n'); 

        // Clear existing table content
        const d1Body = document.getElementById('dist-1-body');
        const d2Body = document.getElementById('dist-2-body');
        if(d1Body) d1Body.innerHTML = '';
        if(d2Body) d2Body.innerHTML = '';

        rows.forEach((row, index) => {
            // Skip headers and empty rows
            if (index < 2 || !row.trim()) return;

            // Split by comma handling quotes
            const cols = row.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/); 

            // Mapping based on your CSV structure:
            // cols[4] = Badge, cols[7] = Name, cols[8] = Status, cols[9] = Joined
            const badge = cols[4] ? cols[4].trim() : "";
            const name = cols[7] ? cols[7].replace(/"/g, "").trim() : "";
            const status = cols[8] ? cols[8].trim() : "";
            const joined = cols[9] ? cols[9].trim() : "N/A";

            // Only proceed if we have a valid name and badge
            if (name && badge && status === "Active") {
                
                // Realism check: Determine District
                // If Badge starts with 1 or 2, put in Dist 1. If 3 or 4, put in Dist 2.
                let district = (badge.startsWith('1') || badge.startsWith('2')) ? '1' : '2';

                const targetBody = document.getElementById(`dist-${district}-body`);
                
                if (targetBody) {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td class="col-id">${badge}</td>
                        <td><span class="rank-cell">Officer</span> ${name}</td>
                        <td class="date-col">${joined}</td>
                        <td class="date-col">--</td>
                        <td><span class="tag fto">ACTIVE</span></td>
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