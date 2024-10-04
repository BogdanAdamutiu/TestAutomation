@IsTest
private class AccountWrapperTests {
  @TestSetup
  static void loadTestData() {
    // GIVEN
    Account acct = TestFactory.getAccount('ACME', true);
    List<Opportunity> opps = TestFactory.generateOppsForAccount(acct.id, 1000.00, 5);
    insert opps;
  }

  @IsTest
  static void testPositiveRoundedAveragePrice() {
    // WHEN
    Account acct = [SELECT Id FROM Account LIMIT 1];
    AccountWrapper acctWrapper = new AccountWrapper(acct);
    // THEN
    Test.startTest();
      Assert.areEqual(
        acctWrapper.getRoundedAvgPriceOfOpps(),
        1000.00,
        'Expected to get 1000.00');
    Test.stopTest();
  }
    
  @IsTest
  static void testNegativeAccountWrapperAvgPriceOfOpps() {
  // GIVEN
  Account acct = [SELECT Id FROM Account LIMIT 1];
  List<Opportunity> opps = [
    SELECT Amount
    FROM Opportunity
    WHERE accountId = :acct.Id
  ];
  for(Opportunity opp : opps) {
    opp.Amount = 0;
  }
  update opps;
  AccountWrapper acctWrapper = new AccountWrapper(acct);
  // WHEN
  try {
    Test.startTest();
      acctWrapper.getRoundedAvgPriceOfOpps();
    Test.stopTest();
    Assert.fail('An exception should have been thrown');
  } catch (AccountWrapper.AWException e) {
    // THEN
    Assert.isTrue(
      e.getMessage().equalsIgnoreCase('no won opportunities'),
      'Exception message does not match: ' + e.getMessage()
    );
  }
  }
    
  @IsTest
  static void FailingTest() {
    // WHEN
    Account acct = [SELECT Id FROM Account LIMIT 1];
    AccountWrapper acctWrapper = new AccountWrapper(acct);
    // THEN
    Test.startTest();
      Assert.areEqual(
        acctWrapper.getRoundedAvgPriceOfOpps(),
        10.00,
        'Expected to get 1000.00');
    Test.stopTest();
  }
}