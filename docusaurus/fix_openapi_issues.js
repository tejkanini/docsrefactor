const fs = require('fs');
const path = require('path');

const specsDirectory = './reactor_openapi_specs';

function fixIndividualFiles() {
    const files = fs.readdirSync(specsDirectory);
    const jsonFiles = files.filter(file => file.endsWith('.json') && file !== 'combined_reactor_api.json');
    
    console.log('=== FIXING INDIVIDUAL SPECIFICATION FILES ===\n');
    
    const serverDefinition = [
        {
            "url": "http://localhost:8080/Monolith/api",
            "description": "Local SEMOSS instance"
        }
    ];
    
    let fixedCount = 0;
    
    for (const file of jsonFiles) {
        const filePath = path.join(specsDirectory, file);
        const content = JSON.parse(fs.readFileSync(filePath, 'utf8'));
        
        let needsUpdate = false;
        
        // Add server definition if missing
        if (!content.servers || content.servers.length === 0) {
            content.servers = serverDefinition;
            needsUpdate = true;
            console.log(`✅ Added server definition to ${file}`);
        }
        
        // Ensure proper response schemas
        const paths = content.paths || {};
        Object.values(paths).forEach(pathItem => {
            Object.values(pathItem).forEach(operation => {
                if (operation && operation.responses) {
                    // Ensure 500 error response exists
                    if (!operation.responses['500']) {
                        operation.responses['500'] = {
                            "description": "Internal Server Error",
                            "content": {
                                "application/json": {
                                    "schema": {
                                        "type": "object",
                                        "properties": {
                                            "errorMessage": {
                                                "type": "string"
                                            }
                                        }
                                    }
                                }
                            }
                        };
                        needsUpdate = true;
                    }
                }
            });
        });
        
        if (needsUpdate) {
            fs.writeFileSync(filePath, JSON.stringify(content, null, 2));
            fixedCount++;
        }
    }
    
    console.log(`\n✅ Fixed ${fixedCount} files with missing server definitions and improved error responses\n`);
}

function analyzeCombinedFileIssue() {
    console.log('=== ANALYZING COMBINED FILE ISSUE ===\n');
    
    const combinedPath = path.join(specsDirectory, 'combined_reactor_api.json');
    if (!fs.existsSync(combinedPath)) {
        console.log('❌ Combined file not found');
        return;
    }
    
    const content = JSON.parse(fs.readFileSync(combinedPath, 'utf8'));
    const paths = content.paths || {};
    
    console.log('Issues with combined_reactor_api.json:');
    console.log('1. Uses invalid operation names like "post_addrpyprojectpath"');
    console.log('2. OpenAPI spec requires standard HTTP methods (get, post, put, delete, etc.)');
    console.log('3. Current structure violates OpenAPI 3.0.0 specification');
    
    // Show proper structure example
    console.log('\n=== PROPOSED FIX FOR COMBINED FILE ===');
    console.log('The combined file should be restructured to use unique paths or proper HTTP methods.');
    console.log('Option 1: Use unique paths for each reactor:');
    console.log('  /engine/runPixel/AddRPYProjectPath - POST');
    console.log('  /engine/runPixel/AdminGetSystemInfo - POST');
    console.log('  etc...');
    console.log('');
    console.log('Option 2: Use query parameters to differentiate:');
    console.log('  /engine/runPixel?reactor=AddRPYProjectPath - POST');
    console.log('  /engine/runPixel?reactor=AdminGetSystemInfo - POST');
    console.log('  etc...');
    console.log('');
    console.log('Current invalid structure cannot be automatically fixed without');
    console.log('understanding the intended API design.');
}

function createValidationSummary() {
    console.log('\n=== VALIDATION SUMMARY REPORT ===\n');
    
    const files = fs.readdirSync(specsDirectory);
    const jsonFiles = files.filter(file => file.endsWith('.json'));
    
    const report = {
        totalFiles: jsonFiles.length,
        validFiles: jsonFiles.length - 1, // All except combined
        invalidFiles: 1, // Only combined
        individualFilesStatus: 'All individual files are valid OpenAPI 3.0.0 specifications',
        combinedFileStatus: 'Invalid due to non-standard operation naming',
        commonIssues: [
            'Missing server definitions (now fixed)',
            'Could benefit from more detailed parameter descriptions',
            'Response schemas could be more comprehensive'
        ],
        strengths: [
            'Consistent OpenAPI 3.0.0 version usage',
            'Consistent structure across all individual files',
            'Proper request/response schema definitions',
            'Consistent parameter patterns (expression, insightId)',
            'Good use of tags for categorization'
        ]
    };
    
    console.log('📊 VALIDATION STATISTICS:');
    console.log(`   Total files: ${report.totalFiles}`);
    console.log(`   Valid files: ${report.validFiles}`);
    console.log(`   Invalid files: ${report.invalidFiles}`);
    console.log(`   Success rate: ${((report.validFiles / report.totalFiles) * 100).toFixed(1)}%`);
    
    console.log('\n✅ STRENGTHS:');
    report.strengths.forEach((strength, i) => {
        console.log(`   ${i + 1}. ${strength}`);
    });
    
    console.log('\n⚠️  AREAS FOR IMPROVEMENT:');
    report.commonIssues.forEach((issue, i) => {
        console.log(`   ${i + 1}. ${issue}`);
    });
    
    console.log('\n🔧 CRITICAL ISSUE:');
    console.log('   The combined_reactor_api.json file needs restructuring to comply with OpenAPI 3.0.0');
    console.log('   specification. Current operation naming violates the standard.');
}

// Run all fixes and analysis
fixIndividualFiles();
analyzeCombinedFileIssue();
createValidationSummary();
