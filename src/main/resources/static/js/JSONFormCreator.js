var Section = JSONSchemaForm.default;

var schema = {
	"title": "Monster",
	"description": "Monster statistics for Pathfinder 2E",
	"definitions": {
		"savingThrow": {
			"type": "object",
			"properties": {
				"bonus": {
					"type": "integer",
					"default": 0
				},
				"conditionalBonuses": {
					"type": "array",
					"items": {
						"$ref": "#/definitions/conditionalBonus"
					},
					"default": []
				}
			},
			"required": ["bonus", "conditionalBonuses"]
		},
		"conditionalBonus": {
			"type": "object",
			"properties": {
				"against": {
					"type": "string",
					"minLength": 1
				},
				"bonus": {
					"type": "integer"
				},
				"type": {
					"type": "string",
					"enum": ["CONDITIONAL"],
					"default": "CONDITIONAL"
				}
			},
			"required": ["against", "bonus", "type"]
		},
		"resistanceOrWeakness": {
			"type": "object",
			"properties": {
				"to": {
					"type": "string",
					"minLength": 1
				},
				"amount": {
					"type": "integer"
				},
				"exceptions": {
					"type": "array",
					"items": {
						"type": "string",
						"minLength": 1
					}
				}
			},
			"required": ["to", "amount", "exceptions"]
		},
		"rangedAbility": {
			"type": "object",
			"properties": {
				"ability": {
					"type": "string",
					"minLength": 1
				},
				"range": {
					"description": "Ability range in feet",
					"type": "integer",
					"exclusiveMinimum": 0
				}
			},
			"required": ["ability", "range"]
		},
		"baseAbility": {
			"type": "object",
			"properties": {
				"action": {
					"type": "string",
					"enum": ["NONE", "FREE_ACTION", "REACTION", "ACTION", "TWO_ACTIONS", "THREE_ACTIONS"],
					"default": "NONE"
				},
				"name": {
					"type": "string",
					"minLength": 1
				},
				"type": {
					"type": "string",
					"enum": ["DEFENSIVE_OR_REACTIONARY", "INTERACTION", "OFFENSIVE_OR_PROACTIVE"]
				},
				"traits": {
					"type": "array",
					"items": {
						"type": "string",
						"minLength": 1
					},
					"uniqueItems": true
				}
			},
			"required": ["action", "name", "type", "traits"]
		},
		"dice": {
			"type": "object",
			"properties": {
				"dice": {
					"type": "integer"
				},
				"sides": {
					"type": "integer"
				}
			},
			"required": ["dice", "sides"]
		}
	},
	"type": "object",
	"properties": {
		"metaInformation": {
			"description": "Meta information regarding this object: publishing information, internal system data, etc",
			"type": "object",
			"properties": {
				"objectData": {
					"description": "Various properties that are relevant in our data management system",
					"type": "object",
					"properties": {
						"internalId": {
							"type": "integer"
						},
						"version": {
							"type": "integer"
						}
					},
					"required": ["internalId", "version"]
				},
				"publicationData": {
					"description": "Publication related information",
					"type": "object",
					"properties": {
						"sourceBook": {
							"type": "string",
							"minLength": 1
						},
						"page": {
							"type": "integer"
						},
						"publisher": {
							"type": "string",
							"minLength": 1
						},
						"license": {
							"type": "string",
							"enum": ["OGL", "RESTRICTED"],
							"default": "OGL"
						}
					},
					"required": ["sourceBook", "page", "publisher", "license"]
				},
				"pathfinderSocietyData": {
					"description": "Pathfinder society related data",
					"type": "object",
					"properties": {
						"legal": {
							"type": "boolean",
							"default": true
						}
					},
					"required": ["legal"]
				}
			},
			"required": ["objectData", "publicationData"]
		},
		"traits": {
			"description": "List of traits of this monster",
			"type": "array",
			"items": {
				"type": "string",
				"minLength": 1
			},
			"minItems": 1,
			"uniqueItems": true
		},
		"names": {
			"description": "List of names of this monster",
			"type": "array",
			"items": {
				"type": "string",
				"minLength": 1
			},
			"minItems": 1,
			"uniqueItems": true
		},
		"description": {
			"description": "Description of the monster",
			"type": "string"
		},
		"rarity": {
			"description": "Rarity of the monster.",
			"type": "string",
			"enum": ["COMMON", "UNCOMMON", "RARE", "UNIQUE"],
			"default": "COMMON"
		},
		"challengeRating": {
			"description": "Challenge rating of the monster",
			"type": "integer",
			"minimum": 0
		},
		"perception": {
			"description": "Perception stats and abilities of the monster",
			"type": "object",
			"properties": {
				"bonus": {
					"type": "integer"
				},
				"abilities": {
					"type": "array",
					"items": {
						"type": "string"
					},
					"default": [],
					"uniqueItems": true
				},
				"rangedAbilities": {
					"type": "array",
					"items": {
						"$ref": "#/definitions/rangedAbility"
					},
					"default": [],
					"uniqueItems": true
				}
			},
			"required": ["bonus", "abilities", "rangedAbilities"]
		},
		"communication": {
			"description": "Communication stats and abilities of the monster",
			"type": "object",
			"properties": {
				"languages": {
					"description": "List of languages this monster knows",
					"type": "array",
					"items": {
						"type": "string",
						"minLength": 1
					},
					"default": [],
					"uniqueItems": true
				},
				"abilities": {
					"type": "array",
					"items": {
						"type": "string",
						"minLength": 1
					},
					"default": [],
					"uniqueItems": true
				},
				"rangedAbilities": {
					"type": "array",
					"items": {
						"$ref": "#/definitions/rangedAbility"
					},
					"default": [],
					"uniqueItems": true
				}
			},
			"required": ["languages", "abilities", "rangedAbilities"]
		},
		"statistics": {
			"description": "Basic stats of the monster",
			"type": "object",
			"properties": {
				"charisma": {
					"type": "integer",
					"default": 0
				},
				"constitution": {
					"type": "integer",
					"default": 0
				},
				"dexterity": {
					"type": "integer",
					"default": 0
				},
				"intelligence": {
					"type": "integer",
					"default": 0
				},
				"strength": {
					"type": "integer",
					"default": 0
				},
				"wisdom": {
					"type": "integer",
					"default": 0
				}
			},
			"required": ["charisma", "constitution", "dexterity", "intelligence", "strength", "wisdom"]
		},
		"skills": {
			"description": "Skills of the monster",
			"type": "object",
			"properties": {
				"bonus": {
					"type": "integer"
				},
				"specificSkills": {
					"type": "array",
					"items": {
						"description": "Skill with skill-specific bonus",
						"type": "object",
						"properties": {
							"skill": {
								"type": "string",
								"minLength": 1
							},
							"bonus": {
								"type": "integer"
							}
						},
						"required": ["skill", "bonus"]
					},
					"default": [],
					"uniqueItems": true
				}
			},
			"required": ["bonus", "specificSkills"]
		},
		"items": {
			"description": "List of items the monster has on it",
			"type": "array",
			"items": {
				"type": "object",
				"properties": {
					"item": {
						"type": "string",
						"minLength": 1
					},
					"amount": {
						"type": "integer",
						"minimum": 1,
						"default": 1
					}
				},
				"required": ["item", "amount"]
			},
			"default": [],
			"uniqueItems": true
		},
		"armorClass": {
			"description": "Armor classes of the monster",
			"type": "object",
			"properties": {
				"regular": {
					"type": "integer"
				},
				"touch": {
					"type": "integer"
				}
			},
			"required": ["regular", "touch"]
		},
		"savingThrows": {
			"description": "Saving throws of the monster",
			"type": "object",
			"properties": {
				"fortitude": {
					"$ref": "#/definitions/savingThrow"
				},
				"reflex": {
					"$ref": "#/definitions/savingThrow"
				},
				"willpower": {
					"$ref": "#/definitions/savingThrow"
				},
				"conditionalBonuses": {
					"type": "array",
					"items": {
						"$ref": "#/definitions/conditionalBonus"
					},
					"default": [],
					"uniqueItems": true
				}
			},
			"required": ["fortitude", "reflex", "willpower", "conditionalBonuses"]
		},
		"hitpoints": {
			"description": "Hitpoints of the monster",
			"type": "integer",
			"minimum": 1
		},
		"immunities": {
			"description": "List of immunities this monster has",
			"type": "array",
			"items": {
				"type": "string",
				"minLength": 1
			},
			"default": [],
			"uniqueItems": true
		},
		"resistances": {
			"description": "List of resistances this monster has",
			"type": "array",
			"items": {
				"$ref": "#/definitions/resistanceOrWeakness"
			},
			"default": [],
			"uniqueItems": true
		},
		"weaknesses": {
			"description": "List of weaknesses this monster has",
			"type": "array",
			"items": {
				"$ref": "#/definitions/resistanceOrWeakness"
			},
			"default": [],
			"uniqueItems": true
		},
		"auras": {
			"description": "List of auras this monster has",
			"type": "array",
			"items": {
				"description": "Aura",
				"type": "object",
				"properties": {
					"name": {
						"type": "string",
						"minLength": 1
					},
					"traits": {
						"type": "array",
						"items": {
							"type": "string",
							"minLength": 1
						},
						"uniqueItems": true,
						"minItems": 1
					},
					"range": {
						"description": "Aura range in feet",
						"type": "integer",
						"minimum": 1
					},
					"description": {
						"type": "string",
						"minLength": 1
					}
				},
				"required": ["name", "traits", "range", "description"]
			},
			"default": [],
			"uniqueItems": true
		},
		"abilities": {
			"type": "array",
			"items": {
				"type": "object",
				"properties": {
					"action": {
						"type": "string",
						"enum": ["NONE", "FREE_ACTION", "REACTION", "ACTION", "TWO_ACTIONS", "THREE_ACTIONS"],
						"default": "NONE"
					},
					"name": {
						"type": "string",
						"minLength": 1
					},
					"type": {
						"type": "string",
						"enum": ["DEFENSIVE_OR_REACTIONARY", "INTERACTION", "OFFENSIVE_OR_PROACTIVE"]
					},
					"traits": {
						"type": "array",
						"items": {
							"type": "string",
							"minLength": 1
						},
						"uniqueItems": true
					},
					"description": {
						"type": "string",
						"minLength": 1
					}
				},
				"required": ["action", "name", "type", "traits", "description"]
			},
			"default": [],
			"uniqueItems": true
		},
		"triggeredAbilities": {
			"type": "array",
			"items": {
				"type": "object",
				"properties": {
					"action": {
						"type": "string",
						"enum": ["NONE", "FREE_ACTION", "REACTION", "ACTION", "TWO_ACTIONS", "THREE_ACTIONS"],
						"default": "NONE"
					},
					"name": {
						"type": "string",
						"minLength": 1
					},
					"type": {
						"type": "string",
						"enum": ["DEFENSIVE_OR_REACTIONARY", "INTERACTION", "OFFENSIVE_OR_PROACTIVE"]
					},
					"traits": {
						"type": "array",
						"items": {
							"type": "string",
							"minLength": 1
						},
						"uniqueItems": true
					},
					"trigger": {
						"type": "string"
					},
					"effect": {
						"type": "string"
					}
				},
				"required": ["action", "name", "type", "traits", "trigger", "effect"]
			},
			"default": [],
			"uniqueItems": true
		},
		"speed": {
			"description": "Monster speeds in feet",
			"type": "object",
			"properties": {
				"burrow": {
					"type": "integer",
					"default": 0,
					"minimum": 0
				},
				"climb": {
					"type": "integer",
					"default": 0,
					"minimum": 0
				},
				"fly": {
					"type": "integer",
					"default": 0,
					"minimum": 0
				},
				"land": {
					"type": "integer",
					"default": 0,
					"minimum": 0
				},
				"swim": {
					"type": "integer",
					"default": 0,
					"minimum": 0
				}
			},
			"required": ["burrow", "climb", "fly", "land", "swim"]
		},
		"rituals": {
			"type": "array",
			"items": {
				"type": "string",
				"minLength": 1
			},
			"default": [],
			"uniqueItems": true
		},
		"attacks": {
			"type": "array",
			"items": {
				"type": "object",
				"properties": {
					"type": {
						"type": "string",
						"enum": ["MELEE", "RANGED"]
					},
					"name": {
						"type": "string",
						"minLength": 1
					},
					"attack": {
						"type": "object",
						"properties": {
							"bonus": {
								"type": "integer"
							},
							"against": {
								"type": "string",
								"enum": ["AC", "TAC"]
							},
							"traits": {
								"type": "array",
								"items": {
									"type": "string",
									"minLength": 1
								}
							},
							"valueTraits": {
								"type": "array",
								"items": {
									"type": "object",
									"properties": {
										"trait": {
											"type": "string",
											"minLength": 1
										},
										"integerValue": {
											"type": "integer"
										},
										"diceValue": {
											"$ref": "#/definitions/dice"
										}
									},
									"required": ["trait"]

								}
							}
						},
						"required": ["bonus", "against", "traits", "valueTraits"]
					},
					"damage": {
						"type": "object",
						"properties": {
							"damageRolls": {
								"type": "array",
								"items": {
									"type": "object",
									"properties": {
										"roll": {
											"$ref": "#/definitions/dice"
										},
										"bonus": {
											"type": "integer"
										},
										"damageType": {
											"type": "string",
											"minLength": 1
										}
									},
									"required": ["roll", "bonus", "damageType"]
								}
							},
							"effects": {
								"type": "array",
								"items": {
									"type": "string",
									"minLength": 1
								}
							}
						},
						"required": ["damageRolls", "effects"]
					}
				},
				"required": ["type", "name", "attack", "damage"]
			},
			"default": [],
			"uniqueItems": true
		},
		"spellCasting": {
			"type": "array",
			"items": {
				"type": "object",
				"properties": {
					"tradition": {
						"type": "string",
						"enum": ["ARCANE", "DIVINE", "OCCULT", "PRIMAL"]
					},
					"type": {
						"type": "string",
						"enum": ["INNATE", "PREPARED", "SPONTANEOUS"]
					},
					"spellDC": {
						"type": "integer"
					},
					"spellAttack": {
						"type": "integer"
					},
					"spellSlots": {
						"description": "Ordered list of available spell slots from level 1 through level 10",
						"type": "array",
						"items": {
							"type": "integer",
							"default": 0
						},
						"minItems": 10,
						"maxItems": 10
					},
					"spells": {
						"type": "array",
						"items": {
							"type": "object",
							"properties": {
								"spell": {
									"type": "string",
									"minLength": 1
								},
								"level": {
									"type": "integer",
									"minimum": 0,
									"maximum": 10
								},
								"availability": {
									"type": "string",
									"enum": ["ALREADY_CAST", "AT_WILL", "CONSTANT", "REGULAR"],
									"default": "REGULAR"
								}
							},
							"required": ["spell", "level"]
						}
					}
				},
				"if": {
					"properties": { "type": { "const": "SPONTANEOUS" } }
				},
				"then": {
					"required": ["tradition", "type", "spellDC", "spellSlots", "spells"]
				},
				"else": {
					"required": ["tradition", "type", "spellDC", "spells"]
				}
			},
			"default": [],
			"uniqueItems": true
		},
		"classPowers": {
			"type": "array",
			"items": {
				"type": "object",
				"properties": {
					"class": {
						"type": "string",
						"enum": ["CLERIC", "DRUID", "SORCERER", "WIZARD"]
					},
					"spellPoints": {
						"type": "integer",
						"minimum": 1
					},
					"powersDC": {
						"type": "integer",
						"minimum": 1
					},
					"powersLevel": {
						"type": "integer",
						"minimum": 1
					},
					"powers": {
						"type": "array",
						"items": {
							"type": "object",
							"properties": {
								"name": {
									"type": "string",
									"minLength": 1
								},
								"cost": {
									"type": "integer",
									"minimum": 1
								}
							},
							"required": ["name", "cost"]
						}
					}
				},
				"required": ["class", "spellPoints", "powersDC", "powersLevel", "powers"]
			},
			"default": [],
			"uniqueItems": true
		}
	},
	"required": ["metaInformation", "traits", "names", "description", "rarity", "challengeRating", "perception", "communication", "statistics", "skills", "items", "armorClass", "savingThrows", "hitpoints", "immunities", "resistances", "weaknesses", "auras", "abilities", "triggeredAbilities", "speed", "rituals", "attacks", "spellCasting", "classPowers"]
};

var log = function log(type) {
	return console.log.bind(console, type);
};
var onSubmit = function onSubmit(_ref, e) {
	var formData = _ref.formData;
	return $("#jsonInput").val(JSON.stringify(formData));
};

var previousFormData = JSON.parse($("#jsonInput").val());

ReactDOM.render(React.createElement(Section, { schema: schema, formData: previousFormData, onChange: log("changed"), onSubmit: onSubmit, onError: log("errors") }), document.getElementById("app"));