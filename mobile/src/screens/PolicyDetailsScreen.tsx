import React from 'react';
import { View, Text, StyleSheet, ActivityIndicator, ScrollView, RefreshControl } from 'react-native';
import { useRoute, RouteProp } from '@react-navigation/native';
import { useGetPolicy } from '../api/generated/endpoints';
import { PoliciesStackParamList } from '../navigation/RootNavigator';

type PolicyDetailsRouteProp = RouteProp<PoliciesStackParamList, 'PolicyDetails'>;

export function PolicyDetailsScreen() {
  const route = useRoute<PolicyDetailsRouteProp>();
  const { policyId } = route.params;

  const { data, isLoading, isError, refetch, isRefetching } = useGetPolicy(policyId);

  if (isLoading && !isRefetching) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#2563EB" />
      </View>
    );
  }

  if (isError) {
    return (
      <View style={styles.centered}>
        <Text style={styles.errorText}>Failed to load policy details.</Text>
      </View>
    );
  }

  const policy = data?.data;

  if (!policy) {
    return (
      <View style={styles.centered}>
        <Text style={styles.errorText}>Policy not found.</Text>
      </View>
    );
  }

  return (
    <ScrollView 
      style={styles.container}
      refreshControl={<RefreshControl refreshing={isRefetching} onRefresh={refetch} />}
    >
      <View style={styles.section}>
        <Text style={styles.label}>Policy Number</Text>
        <Text style={styles.value}>{policy.policyNumber}</Text>
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Status</Text>
        <View style={styles.statusBadge}>
          <Text style={styles.statusText}>{policy.status}</Text>
        </View>
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Premium</Text>
        <Text style={styles.value}>${policy.premium}</Text>
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Customer ID</Text>
        <Text style={styles.value}>{policy.customerId}</Text>
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Branch ID</Text>
        <Text style={styles.value}>{policy.branchId}</Text>
      </View>

    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F3F4F6',
  },
  centered: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorText: {
    color: '#DC2626',
    fontSize: 16,
  },
  section: {
    backgroundColor: '#FFFFFF',
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#E5E7EB',
  },
  label: {
    fontSize: 12,
    color: '#6B7280',
    textTransform: 'uppercase',
    marginBottom: 4,
    fontWeight: '600',
  },
  value: {
    fontSize: 16,
    color: '#111827',
  },
  statusBadge: {
    alignSelf: 'flex-start',
    backgroundColor: '#DEF7EC',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
  },
  statusText: {
    color: '#03543F',
    fontWeight: '500',
    fontSize: 14,
  },
});
