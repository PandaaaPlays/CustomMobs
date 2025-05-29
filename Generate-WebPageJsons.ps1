Write-Host "Please select an option:"
Write-Host "1. Generate Options Json"
Write-Host "2. Generate Custom Effects Json"

$UserChoice = Read-Host "Enter your choice"
Write-Host "===================="

switch ($UserChoice)
{
    "1" {
        $JavaFilesDirectory = ".\src\main\java\ca\pandaaa\custommobs\custommobs\Options"
        Write-Host "Processing .java files in directory: $JavaFilesDirectory"
        $OutputJsonFile = ".\WebPage\src\assets\data\options.json"
    }
    "2" {
        $JavaFilesDirectory = ".\src\main\java\ca\pandaaa\custommobs\custommobs\CustomEffects"
        Write-Host "Processing .java files in directory: $JavaFilesDirectory"
        $OutputJsonFile = ".\WebPage\src\assets\data\custom-effects.json"
    }
    default {
        Write-Host "Invalid selection."
        exit
    }
}

$mobsData = @{}
$JavaFiles = Get-ChildItem -Path $JavaFilesDirectory -Filter *.java

foreach ($file in $JavaFiles)
{
    if ($file.Name -match "CustomMobOption" -or $file.Name -match "CustomEffect") {
        continue
    }

    Write-Host " - Processing file: $( $file.Name )"
    $mobName = ($file.Name -replace "\.java$", "")
    $fields = @()
    $inCommentBlock = $false
    $commentBuffer = @()
    $staticConstants = @{}
    $staticLastLine = $false

    $lines = Get-Content $file.FullName

    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i].Trim()

        if ($line -match 'super\(.*?,\s*CustomEffectType\.([A-Z_]+)\)')
        {
            $customEffectType = $matches[1]
            break
        }

        # Start of JavaDoc
        if ($line -eq '/**')
        {
            $inCommentBlock = $true
            $commentBuffer = @()
            continue
        }

        # End of JavaDoc
        if ($line -eq '*/')
        {
            $inCommentBlock = $false
            continue
        }

        if ($inCommentBlock)
        {
            $commentBuffer += ($line -replace '^\s*\* ?', '')
            continue
        }

        # Match static constants
        if ($line -match 'private\s+static\s+final\s+String\s+(\w+)\s*=\s*"([^"]+)";')
        {
            $constName = $matches[1]
            $description = (($commentBuffer -join ' ') -replace '\s+', ' ' -replace '@minimum\s[-\w.]+', '' -replace '@maximum\s[-\w.]+', '').Trim()

            $minimum = $null
            $maximum = $null
            foreach ($commentLine in $commentBuffer)
            {
                if ($commentLine -match '@minimum\s+(.+)')
                {
                    $minimum = $matches[1]
                }
                if ($commentLine -match '@maximum\s+(.+)')
                {
                    $maximum = $matches[1]
                }
            }

            # Save partial field info for now
            $staticConstants[$constName] = @{
                key = $constName
                description = $description
                minimum = $minimum
                maximum = $maximum
                type = $null
            }

            $staticLastLine = $true
            $commentBuffer = @()
        }

        if ($line -match "private\s+(?!static)(\w.+)\s+" -and $staticLastLine)
        {
            $type = $matches[1]

            switch ($type)
            {
                "int" {
                    $type = "Integer"
                }
                "Double" {
                    $type = "Decimal"
                }
                "DamageRange" {
                    $type = "Range of damage"
                }
                "boolean" {
                    $type = "On / Off"
                }
                "Material" {
                    $type = "Item"
                }
                "UUID" {
                    $type = "Unique user ID"
                }
                default {
                    $type = $type -replace "org.bukkit.entity.", ""
                    $type = $type -replace '\.', ''
                    $type = $type -replace 'Variant', ' variant'
                    $type = $type -replace 'Type', ' type'
                    $type = $type -replace 'Gene', ' gene'
                    $type = $type -replace 'Style', ' style'
                    $type = $type -replace 'Profession', ' profession'
                    $type = $type -replace 'Color', ' color'
                    $type = $type.Substring(0, 1).ToUpper() + $type.Substring(1).ToLower();
                }
            }

            $staticConstants[$constName].type = $type
            $fields += $staticConstants[$constName]
            $staticLastLine = $false
        }
    }

    if($null -eq $customEffectType) {
        $mobsData[$mobName] = $fields
    } else {
        $mobsData[$mobName] = @{
            effectType = $customEffectType
            fields = $fields
        }
    }
}

$jsonOutput = $mobsData | ConvertTo-Json -Depth 10
Set-Content -Path $OutputJsonFile -Value $jsonOutput
Write-Host "JSON file created at: $OutputJsonFile"
